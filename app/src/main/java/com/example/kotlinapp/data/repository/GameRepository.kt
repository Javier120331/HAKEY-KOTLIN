package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.api.RetrofitClient
import com.example.kotlinapp.data.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository {
    
    private val apiService by lazy {
        try {
            RetrofitClient.gameApiService
        } catch (e: Exception) {
            android.util.Log.e("GameRepository", "Error inicializando API: ${e.message}")
            null
        }
    }
    
    suspend fun getGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.w("GameRepository", "API no disponible, usando datos de respaldo")
                    return@withContext getFallbackGames()
                }
                
                val response = service.getGames()
                if (response.isSuccessful && response.body() != null) {
                    android.util.Log.d("GameRepository", "Juegos obtenidos de la API: ${response.body()!!.size}")
                    response.body()!!
                } else {
                    android.util.Log.w("GameRepository", "API respondió pero sin datos, usando respaldo")
                    getFallbackGames()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("GameRepository", "Error al obtener juegos de la API: ${e.message}")
                // Retornar juegos de ejemplo si no hay conexión
                getFallbackGames()
            }
        }
    }
    
    // Juegos de respaldo si la API no está disponible
    private fun getFallbackGames(): List<Game> {
        return listOf(
            Game(
                id = 1,
                title = "Elden Ring",
                price = 59.99,
                description = "Action RPG épico",
                rating = 4.8f,
                image = "https://cdn.dekudeals.com/images/bd818a768677858984549bc6650932248d6b3cb4/w500.jpg"
            ),
            Game(
                id = 2,
                title = "Cyberpunk 2077",
                price = 49.99,
                description = "RPG futurista",
                rating = 4.5f,
                image = "https://cdn.dekudeals.com/images/8f3f5f6a2fd73388734dc420b41947f6c20b9b19/w500.jpg"
            ),
            Game(
                id = 3,
                title = "The Legend of Zelda",
                price = 69.99,
                description = "Aventura de acción",
                rating = 4.9f,
                image = "https://cdn.dekudeals.com/images/638fef81aed234d121b32e666f2e785c9b09c1d1/w500.jpg"
            )
        )
    }
    
    suspend fun getGameById(id: Int): Game? {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("GameRepository", "API no disponible para obtener juego")
                    return@withContext null
                }
                
                val response = service.getGameById(id)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    suspend fun createGame(game: Game): Game? {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("GameRepository", "API no disponible para crear juego")
                    return@withContext null
                }
                
                val response = service.createGame(game)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    suspend fun updateGame(id: Int, game: Game): Game? {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("GameRepository", "API no disponible para actualizar juego")
                    return@withContext null
                }
                
                val response = service.updateGame(id, game)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    suspend fun deleteGame(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("GameRepository", "API no disponible para eliminar juego")
                    return@withContext false
                }
                
                val response = service.deleteGame(id)
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
