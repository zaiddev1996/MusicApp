package com.appsfactory.test.ui.album_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.appsfactory.test.MainDispatcherRule
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.domain.model.track.Track
import com.appsfactory.test.domain.repository.local.LocalFakeRepository
import com.appsfactory.test.domain.repository.remote.LastFMFakeRepository
import com.appsfactory.test.domain.util.UiState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AlbumDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AlbumDetailViewModel

    @Before
    fun setup() {
        viewModel = AlbumDetailViewModel(
            stateHandle = getStateHandle(),
            repository = LastFMFakeRepository(),
            localRepository = LocalFakeRepository()
        )
    }

    @Test
    fun whenOnTrackClicked_returnsOpenTrackUrlEvent() = runTest {
        val testTrack = Track("test", "", Artist("", "", ""), "")
        viewModel.onTrackClicked(testTrack)

        val event = viewModel.uiEvents.first() is AlbumDetailViewModel.AlbumDetailEvent.OpenTrackUrl
        assertThat(event).isTrue()
    }

    @Test
    fun whenInitializedAndApiSendsError_returnsErrorEvent() = runTest {
        val testRepository = LastFMFakeRepository()
        testRepository.setShouldReturnNetworkError(true)
        viewModel = AlbumDetailViewModel(getStateHandle(), testRepository, LocalFakeRepository())

        val event = viewModel.uiEvents.first() is AlbumDetailViewModel.AlbumDetailEvent.ShowError
        assertThat(event).isTrue()
    }

    @Test
    fun whenInitializedAndAlbumIsMarkedAsFavoriteInDB_returnsTrueForIsFavoriteState() {
        val testRepository = LocalFakeRepository()
        val testAlbum = Album("test", "", Artist("test", "", ""), "", listOf())
        testRepository.addAlbums(listOf(testAlbum))

        viewModel = AlbumDetailViewModel(getStateHandle(), LastFMFakeRepository(), testRepository)
        val favoriteState = viewModel.isFavorite.value

        assertThat(favoriteState).isTrue()
    }

    @Test
    fun whenInitializedAndAlbumIsNotMarkedAsFavoriteInDB_returnsFalseForIsFavoriteState() {
        val favoriteState = viewModel.isFavorite.value
        assertThat(favoriteState).isFalse()
    }

    @Test
    fun whenOnFavoriteClicked_returnTrueForFavoriteStateIfAlreadyNotInFavorites() {
        viewModel.onFavoriteClicked()
        val changedFavoriteState = viewModel.isFavorite.value

        assertThat(changedFavoriteState).isTrue()
    }

    @Test
    fun whenOnFavoriteClicked_returnFalseForFavoriteStateIfAlreadyInFavorites() {
        val testRepository = LocalFakeRepository()
        val testAlbum = Album("test", "", Artist("test", "", ""), "", listOf())

        testRepository.addAlbums(listOf(testAlbum))
        viewModel = AlbumDetailViewModel(getStateHandle(), LastFMFakeRepository(), testRepository)
        viewModel.onFavoriteClicked()

        val changedFavoriteState = viewModel.isFavorite.value
        assertThat(changedFavoriteState).isFalse()
    }

    @Test
    fun whenInitializedAndTracksDataIsEmpty_returnsNoDataFoundState() {
        val state = viewModel.uiState.value is UiState.NoDataFound
        assertThat(state).isTrue()
    }

    @Test
    fun whenInitializedAndTracksDataIsNotEmpty_returnsSuccessState() = runTest {
        val testRepository = LastFMFakeRepository()
        val testTrack = Track("test", "", Artist("", "", ""), "")
        testRepository.updateTracks(listOf(testTrack))

        viewModel = AlbumDetailViewModel(getStateHandle(), testRepository, LocalFakeRepository())
        val state = viewModel.uiState.value is UiState.Success

        assertThat(state).isTrue()
    }

    private fun getStateHandle(): SavedStateHandle {
        return SavedStateHandle().apply {
            set("album", Album("test", "", Artist("test", "", ""), "", listOf()))
        }
    }
}