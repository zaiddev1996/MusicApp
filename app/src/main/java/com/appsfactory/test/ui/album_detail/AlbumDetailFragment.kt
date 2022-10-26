package com.appsfactory.test.ui.album_detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.BlurTransformation
import com.appsfactory.test.R
import com.appsfactory.test.databinding.FragmentAlbumDetailBinding
import com.appsfactory.test.domain.util.UiState
import com.appsfactory.test.utils.extensions.isVisible
import com.appsfactory.test.utils.extensions.openUrl
import com.appsfactory.test.utils.extensions.progressDialog
import com.appsfactory.test.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AlbumDetailViewModel>()
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAlbumDetailBinding.bind(view)

        progressDialog = progressDialog()

        trackAdapter = TrackAdapter {
            viewModel.onTrackClicked(it)
        }

        binding.apply {
            trackRecyclerView.adapter = trackAdapter
            favoriteAlbum.setOnClickListener {
                viewModel.onFavoriteClicked()
            }
        }

        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.album.collectLatest { album ->
                        binding.apply {
                            albumName.text = album.name
                            artistName.text = album.artist.name
                            albumImageView.load(album.imageUrl) {
                                crossfade(durationMillis = 500)
                                transformations(
                                    BlurTransformation(
                                        context = requireContext(),
                                        radius = 25f,
                                        sampling = 2f
                                    )
                                )
                                build()
                            }
                        }
                    }
                }
                launch {
                    viewModel.uiState.collectLatest { state ->
                        when (state) {
                            is UiState.Idle -> return@collectLatest
                            is UiState.Loading -> progressDialog.show()
                            is UiState.Success -> trackAdapter.submitList(state.data)
                            is UiState.NoDataFound -> trackAdapter.submitList(null)
                        }

                        binding.apply {
                            noResultsFound.root.isVisible = state is UiState.NoDataFound
                            favoriteAlbum.isEnabled =
                                state is UiState.Success || state is UiState.NoDataFound
                        }
                        progressDialog.isVisible(state is UiState.Loading)
                    }
                }
                launch {
                    viewModel.uiEvents.collectLatest { event ->
                        when (event) {
                            is AlbumDetailViewModel.AlbumDetailEvent.ShowError -> {
                                showToast(event.msg)
                                progressDialog.dismiss()
                            }
                            is AlbumDetailViewModel.AlbumDetailEvent.OpenTrackUrl -> {
                                openUrl(event.url)
                            }
                        }
                    }
                }
                launch {
                    viewModel.isFavorite.collectLatest { isFavorite ->
                        binding.favoriteAlbum.setImageResource(
                            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                        )
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