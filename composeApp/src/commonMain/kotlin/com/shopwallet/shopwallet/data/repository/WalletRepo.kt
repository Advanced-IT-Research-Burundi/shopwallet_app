package com.shopwallet.shopwallet.data.repository

import com.shopwallet.shopwallet.data.local.AppPreferenceManager
import com.shopwallet.shopwallet.data.model.CartItem
import com.shopwallet.shopwallet.data.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface WalletRepo {
    fun getBalance(): Double
    fun updateBalance(newBalance: Double)
    fun getTransactions(): List<Transaction>
    fun addTransaction(transaction: Transaction)
    
    fun getCart(brandId: String): List<CartItem>
    fun saveCart(brandId: String, items: List<CartItem>)
}

class WalletRepoImpl(
    private val prefs: AppPreferenceManager
) : WalletRepo {
    private var _balance = 124500.50 // Mock initial balance
    
    override fun getBalance(): Double = _balance
    override fun updateBalance(newBalance: Double) {
        _balance = newBalance
    }
    
    override fun getTransactions(): List<Transaction> = emptyList() // Mock
    override fun addTransaction(transaction: Transaction) {} // Mock
    
    override fun getCart(brandId: String): List<CartItem> = prefs.getCart(brandId)
    override fun saveCart(brandId: String, items: List<CartItem>) = prefs.saveCart(brandId, items)
}
