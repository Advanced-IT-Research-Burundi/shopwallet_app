package com.shopwallet.shopwallet.data

import androidx.compose.ui.graphics.Color

data class Brand(
    val id: String,
    val name: String,
    val description: String,
    val logo: String,
    val coverImage: String,
    val primaryColor: Long // Color as Long (ARGB)
)

data class Category(
    val id: String,
    val name: String
)

data class Product(
    val id: String,
    val brandId: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String
)

val brands = listOf(
    Brand(
        id = "1",
        name = "Nike",
        description = "Premium fashion for the modern era",
        logo = "https://i.ebayimg.com/images/g/GooAAOSw9ANipV8u/s-l1200.jpg",
        coverImage = "https://static.nike.com/a/images/f_auto/2e8d9338-b43d-4ef5-96e1-7fdcfd838f8e/image.jpg",
        primaryColor = 0xFF000000 // Black
    ),
    Brand(
        id = "2",
        name = "Tech Haven",
        description = "Latest gadgets and electronics",
        logo = "https://example.com/logo2.png",
        coverImage = "https://example.com/cover2.png",
        primaryColor = 0xFF0055FF // Blue
    )
)

val categories = listOf(
    Category("all", "All"),
    Category("clothing", "Clothing"),
    Category("shoes", "Shoes"),
    Category("accessories", "Accessories"),
    Category("electronics", "Electronics")
)

val products = listOf(
    Product(
        id = "1",
        brandId = "1",
        name = "Premium Cotton T-Shirt",
        description = "High-quality cotton t-shirt with modern fit",
        price = 29.99,
        category = "clothing",
        image = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500"
    ),
    Product(
        id = "2",
        brandId = "1",
        name = "Slim Fit Jeans",
        description = "Classic denim jeans with stretch comfort",
        price = 49.99,
        category = "clothing",
        image = "https://images.unsplash.com/photo-1542272454315-4c01d7abdf4a?w=500"
    ),
    Product(
        id = "3",
        brandId = "1",
        name = "Leather Sneakers",
        description = "Handcrafted leather sneakers for daily wear",
        price = 89.99,
        category = "shoes",
        image = "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=500"
    ),
    Product(
        id = "4",
        brandId = "2",
        name = "Wireless Headphones",
        description = "Noise-cancelling over-ear headphones",
        price = 199.99,
        category = "electronics",
        image = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500"
    ),
    Product(
        id = "5",
        brandId = "2",
        name = "Smart Watch",
        description = "Fitness tracker with heart rate monitor",
        price = 149.99,
        category = "electronics",
        image = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500"
    ),
     Product(
        id = "6",
        brandId = "1",
        name = "Designer Sunglasses",
        description = "UV protection with stylish frames",
        price = 129.99,
        category = "accessories",
        image = "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=500"
    )
)
