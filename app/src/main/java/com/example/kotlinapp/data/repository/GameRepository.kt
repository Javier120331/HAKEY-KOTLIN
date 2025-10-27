package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.model.Game

class GameRepository {
    
    private val imageUrl = "https://cdn.dekudeals.com/images/8f3f5f6a2fd73388734dc420b41947f6c20b9b19/w500.jpg"
    
    fun getGames(): List<Game> {
        return listOf(
            Game(
                id = 1,
                name = "Elden Ring",
                price = 59.99,
                description = "Action RPG épico",
                rating = 4.8f,
                imageUrl = imageUrl
            ),
            Game(
                id = 2,
                name = "Cyberpunk 2077",
                price = 49.99,
                description = "RPG futurista",
                rating = 4.5f,
                imageUrl = imageUrl
            ),
            Game(
                id = 3,
                name = "The Legend of Zelda",
                price = 69.99,
                description = "Aventura de acción",
                rating = 4.9f,
                imageUrl = imageUrl
            ),
            Game(
                id = 4,
                name = "Starfield",
                price = 59.99,
                description = "Exploración espacial",
                rating = 4.7f,
                imageUrl = imageUrl
            ),
            Game(
                id = 5,
                name = "Baldur's Gate 3",
                price = 59.99,
                description = "RPG de fantasía",
                rating = 4.9f,
                imageUrl = imageUrl
            ),
            Game(
                id = 6,
                name = "Minecraft",
                price = 19.99,
                description = "Construcción y supervivencia",
                rating = 4.8f,
                imageUrl = imageUrl
            ),
            Game(
                id = 7,
                name = "Fortnite",
                price = 0.00,
                description = "Battle Royale gratuito",
                rating = 4.6f,
                imageUrl = imageUrl
            ),
            Game(
                id = 8,
                name = "The Witcher 3",
                price = 39.99,
                description = "RPG de acción medieval",
                rating = 4.8f,
                imageUrl = imageUrl
            ),
            Game(
                id = 9,
                name = "Hollow Knight",
                price = 14.99,
                description = "Metroidvania desafiante",
                rating = 4.7f,
                imageUrl = imageUrl
            ),
            Game(
                id = 10,
                name = "Stardew Valley",
                price = 14.99,
                description = "Simulador agrícola relajante",
                rating = 4.9f,
                imageUrl = imageUrl
            )
        )
    }
    
    fun getGameById(id: Int): Game? {
        return getGames().find { it.id == id }
    }
}
