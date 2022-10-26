package com.appsfactory.test.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.appsfactory.test.R
import com.appsfactory.test.databinding.FragmentHomeBinding
import com.appsfactory.test.domain.util.UiState
import com.appsfactory.test.ui.album.AlbumAdapter
import com.appsfactory.test.utils.extensions.isVisible
import com.appsfactory.test.utils.extensions.progressDialog
import com.appsfactory.test.utils.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var albumAdapter: AlbumAdapter

    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

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

        initMenu()
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
                            is HomeViewModel.HomeEvent.NavigateToDetailsScreen -> {
                                val direction =
                                    HomeFragmentDirections.actionHomeFragmentToAlbumDetailFragment(
                                        event.album
                                    )
                                findNavController().navigate(direction)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        val direction =
                            HomeFragmentDirections.actionMainActivityToSearchArtistFragment()
                        findNavController().navigate(direction)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}