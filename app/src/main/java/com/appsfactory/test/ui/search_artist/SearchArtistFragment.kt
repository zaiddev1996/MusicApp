package com.appsfactory.test.ui.search_artist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.appsfactory.test.R
import com.appsfactory.test.databinding.FragmentSearchArtistBinding
import com.appsfactory.test.domain.util.UiState
import com.appsfactory.test.utils.extensions.doOnQuerySubmit
import com.appsfactory.test.utils.extensions.isVisible
import com.appsfactory.test.utils.extensions.progressDialog
import com.appsfactory.test.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchArtistFragment : Fragment(R.layout.fragment_search_artist) {

    private var _binding: FragmentSearchArtistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchArtistViewModel>()
    private lateinit var artistAdapter: ArtistAdapter

    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSearchArtistBinding.bind(view)

        progressDialog = progressDialog()

        artistAdapter = ArtistAdapter {
            viewModel.onArtistClicked(it)
        }

        binding.apply {
            artistRecyclerView.adapter = artistAdapter

            searchView.apply {
                doOnQuerySubmit {
                    viewModel.onSearchClicked(it)
                    clearFocus()
                }
                requestFocus()
            }
            searchBtn.setOnClickListener {
                viewModel.onSearchClicked(searchView.query.toString())
                searchView.clearFocus()
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
                            is UiState.Success -> artistAdapter.submitList(state.data)
                            is UiState.NoDataFound -> artistAdapter.submitList(null)
                        }
                        binding.noResultsFound.root.isVisible = state is UiState.NoDataFound
                        progressDialog.isVisible(state is UiState.Loading)
                    }
                }
                launch {
                    viewModel.uiEvents.collectLatest { event ->
                        when (event) {
                            is SearchArtistViewModel.SearchArtistEvent.NavigateToAlbumScreen -> {
                                val direction =
                                    SearchArtistFragmentDirections.actionSearchArtistFragmentToAlbumFragment(
                                        event.artist
                                    )
                                findNavController().navigate(direction)
                            }
                            is SearchArtistViewModel.SearchArtistEvent.ShowError -> {
                                showToast(event.error)
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