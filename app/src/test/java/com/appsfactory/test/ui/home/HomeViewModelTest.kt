package com.appsfactory.test.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appsfactory.test.MainDispatcherRule
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.domain.repository.local.LocalFakeRepository
import com.appsfactory.test.domain.util.UiState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(LocalFakeRepository())
    }

    @Test
    fun whenAlbumsAreEmpty_returnsNoDataFoundState() = runTest {
        val repository = LocalFakeRepository()
        viewModel = HomeViewModel(repository)

        val state = viewModel.uiState.value is UiState.NoDataFound
        assertThat(state).isTrue()
    }

    @Test
    fun whenAlbumsAreNotEmpty_returnsSuccessState() = runTest {
        val repository = LocalFakeRepository()
        repository.insertAlbum(Album("name, ", "", Artist("", "", ""), "", listOf()))

        viewModel = HomeViewModel(repository)

        val state = viewModel.uiState.value is UiState.Success
        assertThat(state).isTrue()
    }

    @Test
    fun whenOnAlbumClicked_returnsNavigateToDetailsScreenEvent() = runTest {
        val testAlbum = Album("test", "", Artist("", "", ""), "", listOf())
        viewModel.onAlbumClicked(testAlbum)

        val event = viewModel.uiEvents.first() is HomeViewModel.HomeEvent.NavigateToDetailsScreen
        assertThat(event).isTrue()
    }

    fun whenInitializedSate_returnsLoadingState() = runTest {
        val repository = LocalFakeRepository()

        viewModel = HomeViewModel(repository)

        val state = viewModel.uiState.value is UiState.Loading
        assertThat(state).isTrue()
    }
}