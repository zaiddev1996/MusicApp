package com.appsfactory.test.ui.album

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.appsfactory.test.R
import com.appsfactory.test.databinding.FragmentAlbumBinding
import com.appsfactory.test.domain.util.UiState
import com.appsfactory.test.utils.extensions.isVisible
import com.appsfactory.test.utils.extensions.progressDialog
import com.appsfactory.test.utils.extensions.show
import com.appsfactory.test.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : Fragment(R.layout.fragment_album) {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AlbumViewModel>()
    private lateinit var albumAdapter: AlbumAdapter

    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAlbumBinding.bind(view)

        progressDialog = progressDialog()

        albumAdapter = AlbumAdapter {
            viewModel.onAlbumClicked(it)
        }

        binding.apply {
            albumRecyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = albumAdapter
            }
        }

        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { state ->
                        when (state) {
                            is UiState.Idle -> return@collectLatest
                            is UiState.Loading -> progressDialog.show()
                            is UiState.Success -> albumAdapter.submitList(state.data)
                            is UiState.NoDataFound -> binding.noResultsFound.root.show()
                        }
                        progressDialog.isVisible(state is UiState.Loading)
                    }
                }
                launch {
                    viewModel.uiEvents.collectLatest { event ->
                        when (event) {
                            is AlbumViewModel.AlbumEvent.NavigateToDetailsScreen -> {
                                val direction =
                                    AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(
                                        event.album
                                    )
                                findNavController().navigate(direction)
                            }
                            is AlbumViewModel.AlbumEvent.ShowError -> {
                                showToast(event.error)
                                progressDialog.dismiss()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}