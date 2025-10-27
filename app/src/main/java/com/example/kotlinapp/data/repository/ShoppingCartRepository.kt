package com.example.kotlinapp.data.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.kotlinapp.data.model.CartItem
import com.example.kotlinapp.data.model.Game

class ShoppingCartRepository {
    
    private val cartItems = mutableStateListOf<CartItem>()
    // Último mensaje de acción (por ejemplo: "Agregado: Nombre del juego")
    var lastActionMessage by mutableStateOf<String?>(null)
    
    fun addToCart(game: Game) {
        val existingItem = cartItems.find { it.game.id == game.id }
        
        if (existingItem != null) {
            // Si ya existe, aumentar cantidad
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = CartItem(game, existingItem.quantity + 1)
            lastActionMessage = "Agregado: ${game.name} (x${existingItem.quantity + 1})"
        } else {
            // Si no existe, agregar nuevo item
            cartItems.add(CartItem(game, 1))
            lastActionMessage = "Agregado: ${game.name}"
        }
    }
    
    fun removeFromCart(gameId: Int) {
        cartItems.removeAll { it.game.id == gameId }
    }
    
    fun decreaseQuantity(gameId: Int) {
        val item = cartItems.find { it.game.id == gameId }
        if (item != null) {
            if (item.quantity > 1) {
                val index = cartItems.indexOf(item)
                cartItems[index] = CartItem(item.game, item.quantity - 1)
            } else {
                removeFromCart(gameId)
            }
        }
    }
    
    fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }
    
    fun clearCart() {
        cartItems.clear()
    }
    
    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.game.price * it.quantity }
    }
    
    fun getCartCount(): Int {
        return cartItems.size
    }
}
