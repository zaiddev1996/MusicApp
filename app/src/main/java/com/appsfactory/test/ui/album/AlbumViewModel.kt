package com.appsfactory.test.ui.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsfactory.test.domain.model.album.Album
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
class AlbumViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val repository: LastFMRepository
) : ViewModel() {

    private val artist = stateHandle.get<Artist>("artist")!!

    private val _uiState = MutableStateFlow<UiState<List<Album>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<AlbumEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        getTopAlbums(artist)
    }

    private fun getTopAlbums(artist: Artist) = viewModelScope.launch {
        repository.getTopAlbumsOfArtist(artist.name).collectLatest { result ->
            when (result) {
                is Result.Success -> {
                    val albums = result.data
                    if (albums.isEmpty()) {
                        _uiState.emit(UiState.NoDataFound)
                    } else {
                        _uiState.emit(UiState.Success(albums))
                    }
                }
                is Result.Error -> {
                    _uiEvents.send(AlbumEvent.ShowError(result.error))
                }
            }
        }
    }

    fun onAlbumClicked(album: Album) = viewModelScope.launch {
        _uiEvents.send(AlbumEvent.NavigateToDetailsScreen(album))
    }

    sealed class AlbumEvent {
        data class NavigateToDetailsScreen(val album: Album) : AlbumEvent()
        data class ShowError(val error: String) : AlbumEvent()
    }
}