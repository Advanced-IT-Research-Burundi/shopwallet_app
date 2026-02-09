package com.shopwallet.shopwallet.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.data.remote.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

fun <T> ViewModel.launchWithState(
  stateFlow: MutableStateFlow<UiState<T>>,
  block: suspend () -> Result<ApiResponse<T>>,
  onSuccess: (T?) -> Unit = {},
  onFailure: (Throwable) -> Unit = {},
) {
  stateFlow.value = UiState(isLoading = true)

  viewModelScope.launch {
    block().fold(
      onSuccess = { response ->

        if (response.success == true) {
          stateFlow.value = UiState(
            message = response.message,
            data = response.data
          )
          onSuccess(response.data)
        } else {
          val errorMessage = parseApiError(response.error, response.message)
          stateFlow.value =
            UiState(error = errorMessage)
        }
      },
      onFailure = { e ->
        stateFlow.value = UiState(
          error = e.message ?: "Erreur inconnue"
        )
        onFailure(e)
      }
    )
  }
}

fun parseApiError(errorElement: JsonElement?, fallback: String? = null): String {
  if (errorElement == null) return fallback ?: "Erreur inconnue"
  return try {
    when (errorElement) {
      is JsonObject -> {
        when (val firstValue = errorElement.values.firstOrNull()) {
          is JsonArray -> firstValue.firstOrNull()?.jsonPrimitive?.contentOrNull
          is JsonPrimitive -> firstValue.contentOrNull
          else -> fallback
        } ?: fallback ?: "Erreur inconnue"
      }
      is JsonArray -> {
        errorElement.firstOrNull()?.jsonPrimitive?.contentOrNull ?: fallback ?: "Erreur inconnue"
      }
      is JsonPrimitive -> errorElement.contentOrNull ?: fallback ?: "Erreur inconnue"
    }
  } catch (e: Exception) {
    e.printStackTrace()
    fallback ?: "Erreur inconnue"
  }
}

data class UiState<T>(
  val isLoading: Boolean = false,
  val message: String? = null,
  val error: String? = null,
  val data: T? = null,
)

