package com.appsfactory.test.ui.search_artist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.appsfactory.test.MainDispatcherRule
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
class SearchArtistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchArtistViewModel

    @Before
    fun setup() {
        viewModel = SearchArtistViewModel(LastFMFakeRepository())
    }

    @Test
    fun whenQueryIsNotValid_returnsIdleState() {
        viewModel.onSearchClicked("   ")
        val state = viewModel.uiState.value is UiState.Idle

        assertThat(state).isTrue()
    }

    @Test
    fun whenQueryIsValidAndDataIsEmpty_returnsNoDataFoundState() {
        viewModel.onSearchClicked("eminem")
        val state = viewModel.uiState.value is UiState.NoDataFound

        assertThat(state).isTrue()
    }

    @Test
    fun whenQueryIsValidAndApiSendsError_returnsNoDataFoundState() {
        val testRepository = LastFMFakeRepository()
        testRepository.setShouldReturnNetworkError(true)

        viewModel = SearchArtistViewModel(testRepository)
        viewModel.onSearchClicked("eminem")

        val state = viewModel.uiState.value is UiState.NoDataFound
        assertThat(state).isTrue()
    }

    @Test
    fun whenQueryIsValidAndApiSendsError_returnsErrorEvent() = runTest {
        val testRepository = LastFMFakeRepository()
        testRepository.setShouldReturnNetworkError(true)

        viewModel = SearchArtistViewModel(testRepository)
        viewModel.onSearchClicked("eminem")

        val event = viewModel.uiEvents.first() is SearchArtistViewModel.SearchArtistEvent.ShowError
        assertThat(event).isTrue()
    }

    @Test
    fun whenQueryIsValidAndDataIsNotEmpty_returnsSuccessState() {
        val testRepository = LastFMFakeRepository()
        testRepository.updateArtists(listOf(Artist("test", "", "")))

        viewModel = SearchArtistViewModel(testRepository)
        viewModel.onSearchClicked("eminem")

        val state = viewModel.uiState.value is UiState.Success
        assertThat(state).isTrue()
    }

    @Test
    fun whenOnArtistClicked_returnNavigateToAlbumScreenEvent() = runTest {
        val testArtist = Artist("test", "", "")
        viewModel.onArtistClicked(testArtist)

        val event =
            viewModel.uiEvents.first() is SearchArtistViewModel.SearchArtistEvent.NavigateToAlbumScreen
        assertThat(event).isTrue()
    }
}