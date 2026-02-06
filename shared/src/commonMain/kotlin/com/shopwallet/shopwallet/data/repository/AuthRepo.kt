package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.data.model.*
import com.shopwallet.shopwallet.data.remote.AuthClient

interface AuthRepo {
    suspend fun requestOtp(phone: String): Result<OtpResponse>
    suspend fun verifyOtp(phone: String, otp: String): Result<VerifyOtpResponse>
    suspend fun getUser(): Result<UserResponse>
    suspend fun logout(): Result<Unit>
    fun getToken(): String?
    fun saveToken(token: String?)
    fun clearSession()
}

class AuthRepoImpl(
    private val client: AuthClient,
    private val prefs: AppPreferenceManager,
) : AuthRepo {
    
    override suspend fun requestOtp(phone: String): Result<OtpResponse> {
       return client.requestOtp(phone)
    }
    
    override suspend fun verifyOtp(phone: String, otp: String): Result<VerifyOtpResponse> {
        val result = client.verifyOtp(phone, otp)
        result.onSuccess { response ->
            saveToken(response.token)
        }
        return result
    }
    
    override suspend fun getUser(): Result<UserResponse> {
        return client.getUser()
    }
    
    override suspend fun logout(): Result<Unit> {
        val result = client.logout()
        // Clear session regardless of API success/failure
        clearSession()
        return result
    }
    
    override fun getToken(): String? = prefs.authToken
    
    override fun saveToken(token: String?) {
        prefs.authToken = token
    }
    
    override fun clearSession() {
        prefs.logout()
    }
}
