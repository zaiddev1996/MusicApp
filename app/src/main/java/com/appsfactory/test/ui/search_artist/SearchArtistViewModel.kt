package com.appsfactory.test.ui.search_artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsfactory.test.domain.model.artist.Artist
import com.appsfactory.test.domain.repository.remote.LastFMRepository
import com.appsfactory.test.domain.util.Result
import com.appsfactory.test.domain.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchArtistViewModel @Inject constructor(
    private val repository: LastFMRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Artist>>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<SearchArtistEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    private fun searchArtist(query: String) = viewModelScope.launch {
        repository.searchArtist(query).collectLatest { result ->
            when (result) {
                is Result.Success -> {
                    val artists = result.data
                    if (artists.isEmpty()) {
                        _uiState.emit(UiState.NoDataFound)
                    } else {
                        _uiState.emit(UiState.Success(artists))
                    }
                }
                is Result.Error -> {
                    _uiState.emit(UiState.NoDataFound)
                    _uiEvents.send(SearchArtistEvent.ShowError(result.error))
                }
            }
        }
    }

    fun onSearchClicked(query: String) = viewModelScope.launch {
        if (isQueryValid(query)) {
            _uiState.emit(UiState.Loading)
            searchArtist(query)
        } else {
            _uiState.emit(UiState.Idle)
        }
    }

    private fun isQueryValid(query: String): Boolean = query.isNotBlank()

    fun onArtistClicked(artist: Artist) = viewModelScope.launch {
        _uiEvents.send(SearchArtistEvent.NavigateToAlbumScreen(artist))
    }

    sealed class SearchArtistEvent {
        data class NavigateToAlbumScreen(val artist: Artist) : SearchArtistEvent()
        data class ShowError(val error: String) : SearchArtistEvent()
    }
}