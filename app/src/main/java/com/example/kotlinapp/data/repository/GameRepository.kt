package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.model.Game

class GameRepository {
    
    
    
    fun getGames(): List<Game> {
        return listOf(
            Game(
                id = 1,
                name = "Elden Ring",
                price = 59.99,
                description = "Action RPG épico",
                rating = 4.8f,
                imageUrl = "https://cdn.dekudeals.com/images/bd818a768677858984549bc6650932248d6b3cb4/w500.jpg"
            ),
            Game(
                id = 2,
                name = "Cyberpunk 2077",
                price = 49.99,
                description = "RPG futurista",
                rating = 4.5f,
                imageUrl = "https://cdn.dekudeals.com/images/8f3f5f6a2fd73388734dc420b41947f6c20b9b19/w500.jpg"
            ),
            Game(
                id = 3,
                name = "The Legend of Zelda",
                price = 69.99,
                description = "Aventura de acción",
                rating = 4.9f,
                imageUrl = "https://cdn.dekudeals.com/images/638fef81aed234d121b32e666f2e785c9b09c1d1/w500.jpg"
            ),
            Game(
                id = 4,
                name = "Starfield",
                price = 59.99,
                description = "Exploración espacial",
                rating = 4.7f,
                imageUrl = "https://imgproxy.eneba.games/TGtAnVFGemO9DYGj6q1EgVwf2HsZ9xMgL3SXq8gFHyI/rs:fit:350/ar:1/czM6Ly9wcm9kdWN0/cy5lbmViYS5nYW1l/cy9wcm9kdWN0cy9U/S0VMNmJyZjBvVVJT/NTEtazFxaUpUMXpr/UVR3ZjR2WUpkTElW/WnNSdXhZLmpwZw"
            ),
            Game(
                id = 5,
                name = "Baldur's Gate 3",
                price = 59.99,
                description = "RPG de fantasía",
                rating = 4.9f,
                imageUrl = "https://imgproxy.eneba.games/R8SMud9O9U4podVQ6Q_YgNdLwFNT0g9q_JCyPbetgxw/rs:fit:350/ar:1/czM6Ly9wcm9kdWN0/cy5lbmViYS5nYW1l/cy9wcm9kdWN0cy8w/WUFMb0QydzdfRUpk/UnoyOWlIalNDUUJ2/MVI4Z19IeVlpS05l/SGwwZXJvLmpwZw"
            ),
            Game(
                id = 6,
                name = "Minecraft",
                price = 19.99,
                description = "Construcción y supervivencia",
                rating = 4.8f,
                imageUrl = "https://imgproxy.eneba.games/IRglYcwilNc3GguGGpby0x_x0jFgdFvPVR1ngi-BkR0/rs:fit:350/ar:1/czM6Ly9wcm9kdWN0/cy5lbmViYS5nYW1l/cy9wcm9kdWN0cy9E/YmR4N2loekprNDhi/QVNpX0cwakhFZ3gy/b2JmR1lFX3ROeVdz/anRKNGE0LnBuZw"
            ),
            Game(
                id = 7,
                name = "Fortnite",
                price = 0.00,
                description = "Battle Royale gratuito",
                rating = 4.6f,
                imageUrl = "https://imgproxy.eneba.games/07hUS_EzPg_dAxFd4zW57NOB0ZCmlGRNmPERkep998E/rs:fit:350/ar:1/czM6Ly9wcm9kdWN0/cy5lbmViYS5nYW1l/cy9wcm9kdWN0cy93/VEtENjVUamZrUEdE/bFpmaC1NaFJBVEEw/c3Z6OWZ4akVIRUFN/Ni1tdGx3LmpwZWc"
            ),
            Game(
                id = 8,
                name = "The Witcher 3",
                price = 39.99,
                description = "RPG de acción medieval",
                rating = 4.8f,
                imageUrl = "https://imgproxy.eneba.games/8xM2TkC2YW76pywKD5d92PzCBoDAYSg_TalSblSv0EA/rs:fit:350/ar:1/czM6Ly9wcm9kdWN0/cy5lbmViYS5nYW1l/cy9wcm9kdWN0cy82/UHB2YTgyeUtGOHlu/ZjR6UXhjTVJGOG95/b01weEdvZENsUmJk/VTV6aEI0LmpwZw"
            ),
            Game(
                id = 9,
                name = "Hollow Knight",
                price = 14.99,
                description = "Metroidvania desafiante",
                rating = 4.7f,
                imageUrl = "https://imgproxy.eneba.games/LKR__25HqkYLkhmysnf9r1goZpHFbCf5JeUoAfERT6k/rs:fit:350/ar:1/czM6Ly9wcm9kdWN0/cy5lbmViYS5nYW1l/cy9wcm9kdWN0cy9x/N2ktbXNpS0p3OEE3/M0llTEt0WXV3cHIw/aWx3NjJOV1NWUUlG/RDBHYkIwLmpwZWc"
            ),
            Game(
                id = 10,
                name = "Stardew Valley",
                price = 14.99,
                description = "Simulador agrícola relajante",
                rating = 4.9f,
                imageUrl ="https://imgproxy.eneba.games/Z3g42WNmkSRlJhMpXNjtR-xLpyjo3ODzAnbQp4RUhRc/rs:fit:350/ar:1/czM6Ly9wcm9kdWN0/cy5lbmViYS5nYW1l/cy9wcm9kdWN0cy94/cnBteWRudTlycHh2/eGZqa2l1Ny5qcGc"
            )
        )
    }
    
    fun getGameById(id: Int): Game? {
        return getGames().find { it.id == id }
    }
}
