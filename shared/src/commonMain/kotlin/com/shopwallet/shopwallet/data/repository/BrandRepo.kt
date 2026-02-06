package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.CompanyResponse
import com.shopwallet.shopwallet.data.model.Product
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

interface BrandRepo {
    suspend fun getCompanies(): Result<List<CompanyResponse>>
    suspend fun getCompanyById(id: String): Result<CompanyResponse>
}

class BrandRepoImpl(
    private val client: HttpClient
) : BrandRepo {
    
    override suspend fun getCompanies(): Result<List<CompanyResponse>> {
        return try {
            val response = client.get("companies").body<List<CompanyResponse>>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getCompanyById(id: String): Result<CompanyResponse> {
        return try {
            val response = client.get("companies/$id").body<CompanyResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
