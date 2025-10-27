package com.example.kotlinapp.data.model

data class CartItem(
    val game: Game,
    val quantity: Int = 1
)
