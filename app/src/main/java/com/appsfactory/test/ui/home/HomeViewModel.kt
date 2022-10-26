package com.appsfactory.test.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsfactory.test.domain.model.album.Album
import com.appsfactory.test.domain.repository.local.LocalRepository
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
class HomeViewModel @Inject constructor(
    private val repository: LocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Album>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvents = Channel<HomeEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        getAlbums()
    }

    private fun getAlbums() = viewModelScope.launch {
        repository.getAllAlbums().collectLatest { albums ->
            if (albums.isEmpty()) {
                _uiState.emit(UiState.NoDataFound)
            } else {
                _uiState.emit(UiState.Success(albums))
            }
        }
    }

    fun onAlbumClicked(album: Album) = viewModelScope.launch {
        _uiEvents.send(HomeEvent.NavigateToDetailsScreen(album))
    }

    sealed class HomeEvent {
        data class NavigateToDetailsScreen(val album: Album) : HomeEvent()
    }
}