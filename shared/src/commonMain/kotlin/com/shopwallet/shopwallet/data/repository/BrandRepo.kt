package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.CompanyResponse
import com.shopwallet.shopwallet.data.model.Product
import com.shopwallet.shopwallet.data.model.Subscription
import com.shopwallet.shopwallet.data.model.SubscriptionListResponse
import com.shopwallet.shopwallet.data.model.SubscriptionResponse
import com.shopwallet.shopwallet.data.remote.KtorClient
import com.shopwallet.shopwallet.data.remote.ApiResponse
import com.shopwallet.shopwallet.utils.safeApiCall

interface BrandRepo {
    suspend fun getBrand(brandId: String): Result<ApiResponse<CompanyResponse>>
    suspend fun getProducts(brandId: String): Result<ApiResponse<List<Product>>>
    suspend fun getSubscriptions(): Result<ApiResponse<SubscriptionListResponse>>
    suspend fun getSubscription(subscriptionId: Int): Result<ApiResponse<SubscriptionResponse>>
}

class BrandRepoImpl(
    private val client: KtorClient
) : BrandRepo {

    override suspend fun getBrand(brandId: String): Result<ApiResponse<CompanyResponse>> =
        safeApiCall { client.getBrand(brandId) }

    override suspend fun getProducts(brandId: String): Result<ApiResponse<List<Product>>> =
        safeApiCall { client.getProducts(brandId) }

    override suspend fun getSubscriptions(): Result<ApiResponse<SubscriptionListResponse>> =
        safeApiCall { client.getSubscriptions() }

    override suspend fun getSubscription(subscriptionId: Int): Result<ApiResponse<SubscriptionResponse>> =
        safeApiCall { client.getSubscription(subscriptionId) }
}
