package com.example.kotlinapp.ui.navigation

sealed class NavigationItem(val route: String, val title: String, val icon: String) {
    object Catalog : NavigationItem("catalog", "Catálogo", "📚")
    object Cart : NavigationItem("cart", "Carrito", "🛒")
    // Offers removed from navbar
    object Account : NavigationItem("account", "Mi Cuenta", "👤")
}

val navigationItems = listOf(
    NavigationItem.Catalog,
    NavigationItem.Cart,
    NavigationItem.Account
)
