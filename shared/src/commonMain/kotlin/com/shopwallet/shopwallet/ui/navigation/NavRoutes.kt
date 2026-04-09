package com.shopwallet.shopwallet.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import org.jetbrains.compose.resources.StringResource
import shopwallet.shared.generated.resources.Res
import shopwallet.shared.generated.resources.label_brand
import shopwallet.shared.generated.resources.label_history
import shopwallet.shared.generated.resources.label_wallet

sealed class Screen {
    data object Auth : Screen()
    data class Otp(val phone: String) : Screen()
    data object Brands : Screen()
    data class BrandDetails(val brandId: String) : Screen()
    data class Wallet(val brandId: String) : Screen()
    data class History(val brandId: String) : Screen()
}

sealed class BottomNavScreen(val labelRes: StringResource) {
    data object Wallet : BottomNavScreen(Res.string.label_wallet)
    data object Brand : BottomNavScreen(Res.string.label_brand)
    data object History : BottomNavScreen(Res.string.label_history)
}

class AppNavigator(startDestination: Screen) {
    private val _backStack = mutableStateListOf<Screen>(startDestination)
    val backStack: List<Screen> get() = _backStack

    val currentScreen: Screen
        get() = _backStack.last()

    fun navigate(
        screen: Screen,
        clearStack: Boolean = false,
        popUpTo: Screen? = null,
        popUpToClass: kotlin.reflect.KClass<out Screen>? = null
    ) {
        if (clearStack) {
            _backStack.clear()
            _backStack.add(screen)
        } else if (popUpTo != null) {
            val index = _backStack.indexOf(popUpTo)
            if (index != -1) {
                while (_backStack.size > index + 1) {
                    _backStack.removeLast()
                }
            }
            if (_backStack.lastOrNull() != screen) {
                _backStack.add(screen)
            }
        } else if (popUpToClass != null) {
            val index = _backStack.indexOfLast { it::class == popUpToClass }
            if (index != -1) {
                while (_backStack.size > index + 1) {
                    _backStack.removeLast()
                }
            }
            if (_backStack.lastOrNull() != screen) {
                _backStack.add(screen)
            }
        } else {
            if (_backStack.lastOrNull() != screen) {
                _backStack.add(screen)
            }
        }
    }

    fun popBackStack() {
        if (_backStack.size > 1) {
            _backStack.removeLast()
        }
    }
}
