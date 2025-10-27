package com.example.kotlinapp.data.model

data class Game(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val rating: Float,
    val imageUrl: String
)
