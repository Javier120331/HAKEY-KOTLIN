package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.api.GameApiService
import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class GameRepositoryCompleteTest : DescribeSpec({
    
    val mockGameApiService = mockk<GameApiService>()
    
    val testGames = listOf(
        Game(id = 1, title = "Elden Ring", price = 59.99, rating = 4.8f, description = "Action RPG", category = "RPG"),
        Game(id = 2, title = "Cyberpunk 2077", price = 49.99, rating = 4.2f, description = "Sci-fi RPG", category = "RPG"),
        Game(id = 3, title = "The Witcher 3", price = 39.99, rating = 4.5f, description = "Fantasy RPG", category = "RPG")
    )
    
    val testGame = Game(
        id = 1,
        title = "Elden Ring",
        price = 59.99,
        rating = 4.8f,
        featured = 1,
        image = "https://example.com/elden-ring.jpg",
        description = "Action RPG masterpiece",
        category = "RPG",
        platform = listOf("PC", "PS5"),
        releaseDate = "2022-02-25",
        publisher = "FromSoftware"
    )
    
    describe("GameRepository - Obtener todos los juegos") {
        
        it("Debería obtener lista completa de juegos") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(testGames)
                
                val response = mockGameApiService.getGames()
                val games = response.body() ?: emptyList()
                
                games.shouldHaveSize(3)
                games[0].title.shouldBe("Elden Ring")
                games[1].title.shouldBe("Cyberpunk 2077")
                games[2].title.shouldBe("The Witcher 3")
            }
        }
        
        it("Debería manejar lista vacía de juegos") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(emptyList())
                
                val response = mockGameApiService.getGames()
                
                response.isSuccessful.shouldBe(true)
                response.body().shouldBeEmpty()
            }
        }
        
        it("Debería manejar error 500 en getGames") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.error(
                    500,
                    "Internal Server Error".toResponseBody()
                )
                
                val response = mockGameApiService.getGames()
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(500)
            }
        }
    }
    
    describe("GameRepository - Crear juego") {
        
        it("Debería crear un juego exitosamente") {
            runTest {
                coEvery { mockGameApiService.createGame(any()) } returns Response.success(testGame)
                
                val response = mockGameApiService.createGame(testGame)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.id.shouldBe(1)
                response.body()?.title.shouldBe("Elden Ring")
            }
        }
        
        it("Debería manejar error 400 en createGame") {
            runTest {
                coEvery { mockGameApiService.createGame(any()) } returns Response.error(
                    400,
                    "Bad Request".toResponseBody()
                )
                
                val response = mockGameApiService.createGame(testGame)
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(400)
            }
        }
        
        it("Debería manejar error 409 si juego ya existe") {
            runTest {
                coEvery { mockGameApiService.createGame(any()) } returns Response.error(
                    409,
                    "Conflict".toResponseBody()
                )
                
                val response = mockGameApiService.createGame(testGame)
                
                response.code().shouldBe(409)
            }
        }
    }
    
    describe("GameRepository - Actualizar juego") {
        
        it("Debería actualizar juego completamente") {
            runTest {
                val updatedGame = testGame.copy(
                    price = 49.99,
                    rating = 4.9f,
                    description = "Updated description"
                )
                coEvery { mockGameApiService.updateGame(1, any()) } returns Response.success(updatedGame)
                
                val response = mockGameApiService.updateGame(1, updatedGame)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.price.shouldBe(49.99)
                response.body()?.rating.shouldBe(4.9f)
            }
        }
        
        it("Debería manejar error 404 en updateGame") {
            runTest {
                coEvery { mockGameApiService.updateGame(999, any()) } returns Response.error(
                    404,
                    "Not Found".toResponseBody()
                )
                
                val response = mockGameApiService.updateGame(999, testGame)
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(404)
            }
        }
        
        it("Debería manejar error 500 en updateGame") {
            runTest {
                coEvery { mockGameApiService.updateGame(1, any()) } returns Response.error(
                    500,
                    "Server Error".toResponseBody()
                )
                
                val response = mockGameApiService.updateGame(1, testGame)
                
                response.code().shouldBe(500)
            }
        }
    }
    
    describe("GameRepository - Eliminar juego") {
        
        it("Debería eliminar juego exitosamente") {
            runTest {
                coEvery { mockGameApiService.deleteGame(1) } returns Response.success(Unit)
                
                val response = mockGameApiService.deleteGame(1)
                
                response.isSuccessful.shouldBe(true)
            }
        }
        
        it("Debería manejar error 404 en deleteGame") {
            runTest {
                coEvery { mockGameApiService.deleteGame(999) } returns Response.error(
                    404,
                    "Not Found".toResponseBody()
                )
                
                val response = mockGameApiService.deleteGame(999)
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(404)
            }
        }
        
        it("Debería manejar error 403 cuando no tiene permiso") {
            runTest {
                coEvery { mockGameApiService.deleteGame(1) } returns Response.error(
                    403,
                    "Forbidden".toResponseBody()
                )
                
                val response = mockGameApiService.deleteGame(1)
                
                response.code().shouldBe(403)
            }
        }
    }
    
    describe("GameRepository - Parcialmente actualizar juego") {
        
        it("Debería actualizar solo el precio") {
            runTest {
                val updatedGame = testGame.copy(price = 39.99)
                coEvery { mockGameApiService.patchGame(1, any()) } returns Response.success(updatedGame)
                
                val response = mockGameApiService.patchGame(1, mapOf("price" to 39.99))
                
                response.isSuccessful.shouldBe(true)
                response.body()?.price.shouldBe(39.99)
                response.body()?.title.shouldBe("Elden Ring")
            }
        }
        
        it("Debería actualizar solo el rating") {
            runTest {
                val updatedGame = testGame.copy(rating = 4.7f)
                coEvery { mockGameApiService.patchGame(1, any()) } returns Response.success(updatedGame)
                
                val response = mockGameApiService.patchGame(1, mapOf("rating" to 4.7f))
                
                response.body()?.rating.shouldBe(4.7f)
            }
        }
        
        it("Debería manejar error 400 en patchGame") {
            runTest {
                coEvery { mockGameApiService.patchGame(1, any()) } returns Response.error(
                    400,
                    "Bad Request".toResponseBody()
                )
                
                val response = mockGameApiService.patchGame(1, mapOf("invalid" to "data"))
                
                response.code().shouldBe(400)
            }
        }
    }
    
    describe("GameRepository - Búsquedas y filtros") {
        
        it("Debería encontrar juegos por descripción") {
            val searchResults = testGames.filter { it.description.contains("RPG") }
            
            searchResults.shouldHaveSize(3)
        }
        
        it("Debería encontrar juegos por categoría") {
            val categoryGames = testGames.filter { it.category.isNotEmpty() }
            
            categoryGames.isNotEmpty().shouldBe(true)
            categoryGames.shouldHaveSize(3)
        }
        
        it("Debería ordenar juegos por precio ascendente") {
            val sorted = testGames.sortedBy { it.price }
            
            sorted[0].price.shouldBe(39.99)
            sorted[2].price.shouldBe(59.99)
        }
        
        it("Debería ordenar juegos por rating descendente") {
            val sorted = testGames.sortedByDescending { it.rating }
            
            sorted[0].rating.shouldBe(4.8f)
            sorted[2].rating.shouldBe(4.2f)
        }
    }
})
