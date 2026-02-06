package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

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
    private val client: HttpClient,
    private val prefs: AppPreferenceManager
) : AuthRepo {
    
    override suspend fun requestOtp(phone: String): Result<OtpResponse> {
        return try {
            val response = client.post("auth/request-otp") {
                contentType(ContentType.Application.Json)
                setBody(OtpRequest(phone))
            }.body<OtpResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun verifyOtp(phone: String, otp: String): Result<VerifyOtpResponse> {
        return try {
            val response = client.post("auth/verify-otp") {
                contentType(ContentType.Application.Json)
                setBody(VerifyOtpRequest(phone, otp))
            }.body<VerifyOtpResponse>()
            
            // Save token on successful verification
            saveToken(response.token)
            
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUser(): Result<UserResponse> {
        return try {
            val response = client.get("auth/user").body<UserResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        return try {
            client.post("auth/logout")
            clearSession()
            Result.success(Unit)
        } catch (e: Exception) {
            clearSession() // Clear session even if API call fails
            Result.failure(e)
        }
    }
    
    override fun getToken(): String? = prefs.authToken
    
    override fun saveToken(token: String?) {
        prefs.authToken = token
    }
    
    override fun clearSession() {
        prefs.logout()
    }
}
