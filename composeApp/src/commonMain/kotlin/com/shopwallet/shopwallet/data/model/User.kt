package com.shopwallet.shopwallet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String? = null
)
