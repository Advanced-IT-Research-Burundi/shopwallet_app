package com.shopwallet.shopwallet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shopwallet.shopwallet.data.model.CartItem
import com.shopwallet.shopwallet.data.model.Product

class BrandViewModel : ViewModel() {
    var cartItems by mutableStateOf(listOf<CartItem>())
        private set
    
    var walletBalance by mutableStateOf(124500.50)
        private set

    fun addToCart(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            cartItems = cartItems.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
            }
        } else {
            cartItems = cartItems + CartItem(product)
        }
    }

    fun removeFromCart(productId: String) {
        cartItems = cartItems.filter { it.product.id != productId }
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(productId)
        } else {
            cartItems = cartItems.map {
                if (it.product.id == productId) it.copy(quantity = newQuantity) else it
            }
        }
    }

    fun topUp(amount: Double) {
        walletBalance += amount
    }

    fun confirmPurchase(total: Double) {
        walletBalance -= total
        cartItems = emptyList()
    }
}
