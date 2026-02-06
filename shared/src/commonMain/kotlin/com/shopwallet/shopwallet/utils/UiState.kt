package com.shopwallet.shopwallet.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
    val message: String? = null
)

suspend fun <T> launchWithState(
    stateFlow: MutableStateFlow<UiState<T>>,
    block: suspend () -> Result<T>,
    onSuccess: (T) -> Unit = {},
    onFailure: (Throwable) -> Unit = {}
) {
    stateFlow.update { it.copy(isLoading = true, error = null, message = null) }
    block().onSuccess { result ->
        stateFlow.update { it.copy(isLoading = false, data = result) }
        onSuccess(result)
    }.onFailure { error ->
        stateFlow.update { it.copy(isLoading = false, error = error.message) }
        onFailure(error)
    }
}
