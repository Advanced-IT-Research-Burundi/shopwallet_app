package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.model.Brand
import com.shopwallet.shopwallet.data.model.Product
import com.shopwallet.shopwallet.data.brands
import io.ktor.client.*

interface BrandRepo {
    suspend fun getBrands(): List<Brand>
    suspend fun getBrandById(id: String): Brand?
}

class BrandRepoImpl(
    private val client: HttpClient
) : BrandRepo {
    override suspend fun getBrands(): List<Brand> = brands
    override suspend fun getBrandById(id: String): Brand? = brands.find { it.id == id }
}
