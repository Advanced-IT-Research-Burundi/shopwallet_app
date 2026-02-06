package com.shopwallet.shopwallet.data.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class ApiResponse<T>(
    val success: Boolean? = false,
    val message: String? = null,
    val data: T? = null,
    val error: JsonElement? = null
)

@Serializable
data class EmptyResponse(val status: String? = null)
