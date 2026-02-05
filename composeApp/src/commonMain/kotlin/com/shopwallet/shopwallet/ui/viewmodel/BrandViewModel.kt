package com.shopwallet.shopwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shopwallet.shopwallet.data.model.CartItem
import com.shopwallet.shopwallet.data.model.Product
import com.shopwallet.shopwallet.data.repository.WalletRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BrandViewModel(
    private val brandId: String,
    private val walletRepo: WalletRepo
) : androidx.lifecycle.ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(walletRepo.getCart(brandId))
    val cartItems = _cartItems.asStateFlow()
    
    private val _walletBalance = MutableStateFlow(walletRepo.getBalance())
    val walletBalance = _walletBalance.asStateFlow()

    private fun persistCart() {
        walletRepo.saveCart(brandId, _cartItems.value)
    }

    fun addToCart(product: Product) {
        _cartItems.update { current ->
            val existingItem = current.find { it.product.id == product.id }
            if (existingItem != null) {
                current.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                current + CartItem(product)
            }
        }
        persistCart()
    }

    fun removeFromCart(productId: String) {
        _cartItems.update { current ->
            current.filter { it.product.id != productId }
        }
        persistCart()
    }

    fun updateCartQuantity(productId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(productId)
        } else {
            _cartItems.update { current ->
                current.map {
                    if (it.product.id == productId) it.copy(quantity = newQuantity) else it
                }
            }
            persistCart()
        }
    }

    fun topUp(amount: Double) {
        walletRepo.updateBalance(walletRepo.getBalance() + amount)
        _walletBalance.value = walletRepo.getBalance()
    }

    fun confirmPurchase(total: Double) {
        walletRepo.updateBalance(walletRepo.getBalance() - total)
        _walletBalance.value = walletRepo.getBalance()
        _cartItems.value = emptyList()
        persistCart()
    }
}

