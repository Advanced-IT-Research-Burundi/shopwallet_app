package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.data.model.User
import io.ktor.client.*

interface AuthRepo {
    fun getUser(): User?
    fun getToken(): String?
    fun saveUser(user: User?)
    fun saveToken(token: String?)
    fun clearSession()
}

class AuthRepoImpl(
    private val client: HttpClient,
    private val prefs: AppPreferenceManager
) : AuthRepo {
    override fun getUser(): User? = null // To be implemented with proper User model persistence
    override fun getToken(): String? = prefs.authToken
    override fun saveUser(user: User?) {} // To be implemented
    override fun saveToken(token: String?) {
        prefs.authToken = token
    }
    override fun clearSession() {
        prefs.logout()
    }
}
