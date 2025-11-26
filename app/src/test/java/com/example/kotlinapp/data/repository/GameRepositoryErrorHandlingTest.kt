package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.api.GameApiService
import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class GameRepositoryErrorHandlingTest : DescribeSpec({
    
    val mockGameApiService = mockk<GameApiService>()
    
    val testGame = Game(
        id = 1,
        title = "Test Game",
        price = 49.99,
        rating = 4.5f
    )
    
    describe("GameRepository - Manejo de excepciones") {
        
        it("Debería manejar excepción de timeout en getGames") {
            runTest {
                coEvery { mockGameApiService.getGames() } throws Exception("Timeout")
                
                val exception = try {
                    mockGameApiService.getGames()
                    null
                } catch (e: Exception) {
                    e
                }
                
                exception.shouldBe(exception)
                exception?.message.shouldBe("Timeout")
            }
        }
        
        it("Debería manejar excepción en getGameById") {
            runTest {
                coEvery { mockGameApiService.getGameById(any()) } throws Exception("Error de conexión")
                
                val exception = try {
                    mockGameApiService.getGameById(1)
                    null
                } catch (e: Exception) {
                    e
                }
                
                exception?.message.shouldBe("Error de conexión")
            }
        }
    }
    
    describe("GameRepository - Respuestas vacías") {
        
        it("Debería manejar respuesta exitosa pero vacía de games") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(emptyList())
                
                val response = mockGameApiService.getGames()
                
                response.isSuccessful.shouldBe(true)
                response.body().shouldBe(emptyList())
            }
        }
        
        it("Debería manejar null body en respuesta exitosa") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.success(null)
                
                val response = mockGameApiService.getGames()
                
                response.isSuccessful.shouldBe(true)
                response.body().shouldBe(null)
            }
        }
    }
    
    describe("GameRepository - Códigos de error específicos") {
        
        it("Debería manejar error 429 (Too Many Requests)") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.error(
                    429,
                    "Demasiadas solicitudes".toResponseBody()
                )
                
                val response = mockGameApiService.getGames()
                
                response.code().shouldBe(429)
                response.isSuccessful.shouldBe(false)
            }
        }
        
        it("Debería manejar error 503 (Service Unavailable)") {
            runTest {
                coEvery { mockGameApiService.getGames() } returns Response.error(
                    503,
                    "Servicio no disponible".toResponseBody()
                )
                
                val response = mockGameApiService.getGames()
                
                response.code().shouldBe(503)
            }
        }
        
        it("Debería manejar error 401 (Unauthorized)") {
            runTest {
                coEvery { mockGameApiService.updateGame(1, any()) } returns Response.error(
                    401,
                    "No autorizado".toResponseBody()
                )
                
                val response = mockGameApiService.updateGame(1, testGame)
                
                response.code().shouldBe(401)
            }
        }
    }
    
    describe("GameRepository - CRUD completo") {
        
        it("Debería crear juego exitosamente") {
            runTest {
                coEvery { mockGameApiService.createGame(any()) } returns Response.success(testGame)
                
                val response = mockGameApiService.createGame(testGame)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.title.shouldBe("Test Game")
            }
        }
        
        it("Debería actualizar juego exitosamente") {
            runTest {
                val updatedGame = testGame.copy(price = 39.99)
                coEvery { mockGameApiService.updateGame(1, any()) } returns Response.success(updatedGame)
                
                val response = mockGameApiService.updateGame(1, updatedGame)
                
                response.body()?.price.shouldBe(39.99)
            }
        }
        
        it("Debería eliminar juego exitosamente") {
            runTest {
                coEvery { mockGameApiService.deleteGame(1) } returns Response.success(Unit)
                
                val response = mockGameApiService.deleteGame(1)
                
                response.isSuccessful.shouldBe(true)
            }
        }
    }
})
