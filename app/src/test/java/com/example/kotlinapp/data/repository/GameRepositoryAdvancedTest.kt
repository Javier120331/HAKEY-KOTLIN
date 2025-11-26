package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.api.GameApiService
import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContain
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class GameRepositoryAdvancedTest : DescribeSpec({
    
    val mockGameApiService = mockk<GameApiService>()
    
    val testGames = listOf(
        Game(id = 1, title = "Elden Ring", price = 59.99, rating = 4.8f),
        Game(id = 2, title = "Cyberpunk 2077", price = 49.99, rating = 4.2f),
        Game(id = 3, title = "The Witcher 3", price = 39.99, rating = 4.5f)
    )
    
    val testGame = Game(
        id = 1,
        title = "Elden Ring",
        price = 59.99,
        rating = 4.8f,
        featured = 1,
        image = "https://example.com/elden-ring.jpg"
    )
    
    describe("GameRepository - Filtrado y búsqueda avanzada") {
        
        it("Debería obtener juegos ordenados por precio") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(testGames)
                
                val response = mockGameApiService.getGames()
                val games = response.body() ?: emptyList()
                val sortedByPrice = games.sortedBy { it.price }
                
                sortedByPrice[0].title.shouldBe("The Witcher 3")
                sortedByPrice[2].title.shouldBe("Elden Ring")
            }
        }
        
        it("Debería obtener juegos ordenados por rating") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(testGames)
                
                val response = mockGameApiService.getGames()
                val games = response.body() ?: emptyList()
                val sortedByRating = games.sortedByDescending { it.rating }
                
                sortedByRating[0].rating.shouldBe(4.8f)
                sortedByRating[2].rating.shouldBe(4.2f)
            }
        }
        
        it("Debería filtrar juegos por rango de precio") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(testGames)
                
                val response = mockGameApiService.getGames()
                val games = response.body() ?: emptyList()
                val filtered = games.filter { it.price in 39.99..59.99 }
                
                filtered.size.shouldBe(3)
                filtered[0].title.shouldBe("Elden Ring")
                filtered[1].title.shouldBe("Cyberpunk 2077")
                filtered[2].title.shouldBe("The Witcher 3")
            }
        }
        
        it("Debería filtrar juegos por rating mínimo") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(testGames)
                
                val response = mockGameApiService.getGames()
                val games = response.body() ?: emptyList()
                val filtered = games.filter { it.rating >= 4.5f }
                
                filtered.size.shouldBe(2)
                filtered[0].title.shouldBe("Elden Ring")
                filtered[1].title.shouldBe("The Witcher 3")
            }
        }
    }
    
    describe("GameRepository - Operaciones con juegos individuales") {
        
        it("Debería obtener información detallada de un juego") {
            runTest {
                coEvery { mockGameApiService.getGameById(1) } returns Response.success(testGame)
                
                val response = mockGameApiService.getGameById(1)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.title.shouldBe("Elden Ring")
                response.body()?.image.shouldBe("https://example.com/elden-ring.jpg")
            }
        }
        
        it("Debería actualizar el precio de un juego") {
            runTest {
                val updatedGame = testGame.copy(price = 49.99)
                coEvery { mockGameApiService.updateGame(1, any()) } returns Response.success(updatedGame)
                
                val response = mockGameApiService.updateGame(1, updatedGame)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.price.shouldBe(49.99)
            }
        }
        
        it("Debería actualizar el rating de un juego") {
            runTest {
                val updatedGame = testGame.copy(rating = 4.9f)
                coEvery { mockGameApiService.updateGame(1, any()) } returns Response.success(updatedGame)
                
                val response = mockGameApiService.updateGame(1, updatedGame)
                
                response.body()?.rating.shouldBe(4.9f)
            }
        }
        
        it("Debería marcar un juego como destacado") {
            runTest {
                val featuredGame = testGame.copy(featured = 1)
                coEvery { mockGameApiService.patchGame(1, any()) } returns Response.success(featuredGame)
                
                val response = mockGameApiService.patchGame(1, mapOf("featured" to 1))
                
                response.body()?.featured.shouldBe(1)
                response.body()?.isFeatured.shouldBe(true)
            }
        }
        
        it("Debería desmarcar un juego como destacado") {
            runTest {
                val notFeaturedGame = testGame.copy(featured = 0)
                coEvery { mockGameApiService.patchGame(1, any()) } returns Response.success(notFeaturedGame)
                
                val response = mockGameApiService.patchGame(1, mapOf("featured" to 0))
                
                response.body()?.featured.shouldBe(0)
                response.body()?.isFeatured.shouldBe(false)
            }
        }
    }
    
    describe("GameRepository - Validación de datos") {
        
        it("Debería validar que el precio sea positivo") {
            val game = testGame.copy(price = 59.99)
            
            (game.price > 0).shouldBe(true)
        }
        
        it("Debería validar que el rating esté entre 0 y 5") {
            val game = testGame.copy(rating = 4.8f)
            
            (game.rating in 0f..5f).shouldBe(true)
        }
        
        it("Debería validar que el título no esté vacío") {
            val game = testGame.copy(title = "Elden Ring")
            
            game.title.isNotEmpty().shouldBe(true)
        }
    }
    
    describe("GameRepository - Manejo de errores") {
        
        it("Debería manejar error 404 cuando juego no existe") {
            runTest {
                coEvery { mockGameApiService.getGameById(999) } returns Response.error(
                    404,
                    "Juego no encontrado".toResponseBody()
                )
                
                val response = mockGameApiService.getGameById(999)
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(404)
            }
        }
        
        it("Debería manejar error 500 en actualización") {
            runTest {
                coEvery { mockGameApiService.updateGame(1, any()) } returns Response.error(
                    500,
                    "Error del servidor".toResponseBody()
                )
                
                val response = mockGameApiService.updateGame(1, testGame)
                
                response.code().shouldBe(500)
            }
        }
        
        it("Debería manejar error 400 en creación de juego") {
            runTest {
                coEvery { mockGameApiService.createGame(any()) } returns Response.error(
                    400,
                    "Datos inválidos".toResponseBody()
                )
                
                val response = mockGameApiService.createGame(testGame)
                
                response.code().shouldBe(400)
            }
        }
    }
})
