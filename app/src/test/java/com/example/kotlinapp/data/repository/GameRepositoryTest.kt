package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.api.GameApiService
import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class GameRepositoryTest : BehaviorSpec({
    
    // Tests del GameApiService directamente (sin inyección de dependencias en el repo)
    val mockApiService = mockk<GameApiService>()
    
    Given("Un GameApiService mockeado") {
        
        When("Se llama a getGames() y la API retorna una lista de juegos") {
            val mockGames = listOf(
                Game(
                    id = 1,
                    title = "Elden Ring",
                    price = 59.99,
                    rating = 4.8f,
                    image = "https://example.com/elden-ring.jpg"
                ),
                Game(
                    id = 2,
                    title = "Cyberpunk 2077",
                    price = 49.99,
                    rating = 4.5f,
                    image = "https://example.com/cyberpunk.jpg"
                )
            )
            
            coEvery { mockApiService.getGames() } returns Response.success(mockGames)
            
            Then("Debería retornar la lista de juegos correctamente") {
                runTest {
                    val response = mockApiService.getGames()
                    response.body().shouldBe(mockGames)
                    response.isSuccessful.shouldBe(true)
                }
            }
        }
        
        When("Se llama a getGameById() con un ID válido") {
            val expectedGame = Game(
                id = 1,
                title = "Elden Ring",
                price = 59.99,
                rating = 4.8f,
                image = "https://example.com/elden-ring.jpg"
            )
            
            coEvery { mockApiService.getGameById(1) } returns Response.success(expectedGame)
            
            Then("Debería retornar el juego específico") {
                runTest {
                    val response = mockApiService.getGameById(1)
                    response.body()?.id.shouldBe(1)
                    response.body()?.title.shouldBe("Elden Ring")
                    response.isSuccessful.shouldBe(true)
                }
            }
        }
        
        When("Se llama a createGame() con un nuevo juego") {
            val newGame = Game(
                id = 3,
                title = "The Legend of Zelda",
                price = 69.99,
                rating = 4.9f,
                image = "https://example.com/zelda.jpg"
            )
            
            coEvery { mockApiService.createGame(newGame) } returns Response.success(newGame)
            
            Then("Debería crear el juego exitosamente") {
                runTest {
                    val response = mockApiService.createGame(newGame)
                    response.body()?.id.shouldBe(3)
                    response.isSuccessful.shouldBe(true)
                }
            }
        }
        
        When("Se llama a updateGame() con un juego modificado") {
            val updatedGame = Game(
                id = 1,
                title = "Elden Ring - Deluxe Edition",
                price = 79.99,
                rating = 4.8f,
                image = "https://example.com/elden-ring.jpg"
            )
            
            coEvery { mockApiService.updateGame(1, updatedGame) } returns Response.success(updatedGame)
            
            Then("Debería actualizar el juego exitosamente") {
                runTest {
                    val response = mockApiService.updateGame(1, updatedGame)
                    response.body()?.price.shouldBe(79.99)
                    response.isSuccessful.shouldBe(true)
                }
            }
        }
        
        When("Se llama a deleteGame() con un ID válido") {
            coEvery { mockApiService.deleteGame(1) } returns Response.success(Unit)
            
            Then("Debería eliminar el juego exitosamente") {
                runTest {
                    val response = mockApiService.deleteGame(1)
                    response.isSuccessful.shouldBe(true)
                }
            }
        }
        
        When("La API falla al obtener juegos") {
            coEvery { mockApiService.getGames() } returns Response.error(500, "Error interno".toResponseBody())
            
            Then("Debería retornar una respuesta no exitosa") {
                runTest {
                    val response = mockApiService.getGames()
                    response.isSuccessful.shouldBe(false)
                    response.code().shouldBe(500)
                }
            }
        }
    }
})
