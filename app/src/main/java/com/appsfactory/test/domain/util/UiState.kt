package com.appsfactory.test.domain.util

/**
 * @author Zaid
 */
sealed class UiState<out T : Any> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object NoDataFound : UiState<Nothing>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
}