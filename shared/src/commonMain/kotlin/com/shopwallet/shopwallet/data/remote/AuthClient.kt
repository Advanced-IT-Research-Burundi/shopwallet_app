package com.shopwallet.shopwallet.data.remote

import com.shopwallet.shopwallet.data.model.OtpRequest
import com.shopwallet.shopwallet.data.model.OtpResponse
import com.shopwallet.shopwallet.data.model.UserResponse
import com.shopwallet.shopwallet.data.model.VerifyOtpRequest
import com.shopwallet.shopwallet.data.model.VerifyOtpResponse
import io.ktor.client.request.get
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorClient(private val client: HttpClient, private val baseUrl: String) {
  suspend fun requestOtp(phone: String): Result<OtpResponse> {
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

  suspend fun verifyOtp(phone: String, otp: String): Result<VerifyOtpResponse> {
        return try {
            val response = client.post("auth/verify-otp") {
                contentType(ContentType.Application.Json)
                setBody(VerifyOtpRequest(phone, otp))
            }.body<VerifyOtpResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
  }

    suspend fun getUser(): Result<UserResponse> {
        return try {
            val response = client.get("auth/user").body<UserResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            client.post("auth/logout")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}