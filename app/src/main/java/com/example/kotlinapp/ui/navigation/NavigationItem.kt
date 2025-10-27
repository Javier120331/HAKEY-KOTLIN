package com.example.kotlinapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {
    object Catalog : NavigationItem("catalog", "Catálogo", Icons.Filled.VideogameAsset)
    object Cart : NavigationItem("cart", "Carrito", Icons.Filled.ShoppingCart)
    // Offers removed from navbar
    object Account : NavigationItem("account", "Mi Cuenta", Icons.Filled.AccountCircle)
}

val navigationItems = listOf(
    NavigationItem.Catalog,
    NavigationItem.Cart,
    NavigationItem.Account
)
