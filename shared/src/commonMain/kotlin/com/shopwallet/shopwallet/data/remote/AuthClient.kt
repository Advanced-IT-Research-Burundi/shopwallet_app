package com.shopwallet.shopwallet.data.remote

import com.shopwallet.shopwallet.data.model.OtpRequest
import com.shopwallet.shopwallet.data.model.OtpResponse
import com.shopwallet.shopwallet.data.model.UserResponse
import com.shopwallet.shopwallet.data.model.VerifyOtpRequest
import com.shopwallet.shopwallet.data.model.VerifyOtpResponse
import io.ktor.client.request.get
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthClient(private val client: HttpClient) {
  suspend fun requestOtp(phone: String): ApiResponse<OtpResponse> =
        client.post("auth/request-otp") {
          contentType(ContentType.Application.Json)
          setBody(OtpRequest(phone))
        }.body()

  suspend fun verifyOtp(phone: String, otp: String): ApiResponse<VerifyOtpResponse> =
        client.post("auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(VerifyOtpRequest(phone, otp))
        }.body()

    suspend fun getUser(): ApiResponse<UserResponse> =
        client.get("auth/user").body()

    suspend fun logout(token: String?): ApiResponse<EmptyResponse> =
        client.post("auth/logout") {
          contentType(ContentType.Application.Json)
          if (!token.isNullOrEmpty()) {
            parameter("token", token)
          }
        }.body()
}