package com.shopwallet.shopwallet.data.remote

import com.shopwallet.shopwallet.data.model.CompanyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class BrandClient(private val client: HttpClient) {
    suspend fun getCompanies(): ApiResponse<List<CompanyResponse>> =
        client.get("companies").body()

    suspend fun getCompanyById(id: String): ApiResponse<CompanyResponse> =
        client.get("companies/$id").body()
}
