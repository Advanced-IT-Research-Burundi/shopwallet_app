package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.data.model.*
import com.shopwallet.shopwallet.data.remote.ApiResponse
import com.shopwallet.shopwallet.data.remote.EmptyResponse
import com.shopwallet.shopwallet.data.remote.KtorClient
import com.shopwallet.shopwallet.utils.safeApiCall

interface AuthRepo {
    suspend fun requestOtp(phone: String): Result<ApiResponse<OtpResponse>>
    suspend fun verifyOtp(phone: String, otp: String): Result<ApiResponse<VerifyOtpResponse>>
    suspend fun getUser(): Result<ApiResponse<UserResponse>>
    suspend fun logout(): Result<ApiResponse<EmptyResponse>>
    fun getToken(): String?
    fun saveToken(token: String?)
    fun clearSession()
}

class AuthRepoImpl(
    private val client: KtorClient,
    private val prefs: AppPreferenceManager,
) : AuthRepo {
    
    override suspend fun requestOtp(phone: String): Result<ApiResponse<OtpResponse>> =
        safeApiCall { client.requestOtp(phone) }

    
    override suspend fun verifyOtp(phone: String, otp: String): Result<ApiResponse<VerifyOtpResponse>> {
        val result = safeApiCall { client.verifyOtp(phone, otp) }
        result.onSuccess { response ->
            saveToken(response.data?.token)
        }
        return result
    }
    
    override suspend fun getUser(): Result<ApiResponse<UserResponse>> =
        safeApiCall { client.getUser() }
    
    override suspend fun logout(): Result<ApiResponse<EmptyResponse>> {
        val token = getToken()
        val result = safeApiCall { client.logout(token) }
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
