package com.appsfactory.test.ui.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.appsfactory.test.MainDispatcherRule
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
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
class AlbumViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AlbumViewModel

    @Before
    fun setup() {
        val testRepository = LastFMFakeRepository()
        viewModel = AlbumViewModel(getStateHandle(), testRepository)
    }

    @Test
    fun whenOnAlbumClicked_returnsNavigateToDetailsScreenEvent() = runTest {
        val testAlbum = Album("test", "", Artist("", "", ""), "", listOf())
        viewModel.onAlbumClicked(testAlbum)

        val event = viewModel.uiEvents.first() is AlbumViewModel.AlbumEvent.NavigateToDetailsScreen
        assertThat(event).isTrue()
    }

    @Test
    fun whenInitializedAndApiSendsError_returnsErrorEvent() = runTest {
        val testRepository = LastFMFakeRepository()
        testRepository.setShouldReturnNetworkError(true)
        viewModel = AlbumViewModel(getStateHandle(), testRepository)

        val event = viewModel.uiEvents.first() is AlbumViewModel.AlbumEvent.ShowError
        assertThat(event).isTrue()
    }

    @Test
    fun whenInitializedAndDataIsNotEmpty_returnsSuccessState() = runTest {
        val testRepository = LastFMFakeRepository()
        val testAlbum = Album("test", "", Artist("test", "", ""), "", listOf())
        testRepository.updateAlbums(listOf(testAlbum))

        viewModel = AlbumViewModel(getStateHandle(), testRepository)
        val state = viewModel.uiState.value is UiState.Success

        assertThat(state).isTrue()
    }

    @Test
    fun whenInitializedAndDataIsEmpty_returnsNoDataFoundState() {
        val state = viewModel.uiState.value is UiState.NoDataFound
        assertThat(state).isTrue()
    }

    private fun getStateHandle(): SavedStateHandle {
        return SavedStateHandle().apply {
            set("artist", Artist("test", "", ""))
        }
    }
}