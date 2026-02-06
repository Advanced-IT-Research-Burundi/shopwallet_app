package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.CompanyResponse
import com.shopwallet.shopwallet.data.model.Product
import com.shopwallet.shopwallet.data.remote.BrandClient
import com.shopwallet.shopwallet.data.remote.ApiResponse
import com.shopwallet.shopwallet.utils.safeApiCall

interface BrandRepo {
    suspend fun getCompanies(): Result<ApiResponse<List<CompanyResponse>>>
    suspend fun getCompanyById(id: String): Result<ApiResponse<CompanyResponse>>
}

class BrandRepoImpl(
    private val client: BrandClient
) : BrandRepo {
    
    override suspend fun getCompanies(): Result<ApiResponse<List<CompanyResponse>>> =
        safeApiCall { client.getCompanies() }
    
    override suspend fun getCompanyById(id: String): Result<ApiResponse<CompanyResponse>> =
        safeApiCall { client.getCompanyById(id) }
}
