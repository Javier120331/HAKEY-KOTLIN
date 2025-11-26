package com.example.kotlinapp.data.api

import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class GameApiServiceTest : BehaviorSpec({
    val mockApiService = mockk<GameApiService>()
    
    Given("Un GameApiService mockeado") {
        
        When("Se llama a getGames() y retorna exitosamente") {
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
            
            Then("Debería retornar una lista de juegos") {
                runTest {
                    val response = mockApiService.getGames()
                    response.isSuccessful.shouldBe(true)
                    response.body()?.size.shouldBe(2)
                    response.body()?.first()?.title.shouldBe("Elden Ring")
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
                    response.isSuccessful.shouldBe(true)
                    response.body()?.id.shouldBe(1)
                    response.body()?.title.shouldBe("Elden Ring")
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
            
            Then("Debería crear el juego y retornar su respuesta") {
                runTest {
                    val response = mockApiService.createGame(newGame)
                    response.isSuccessful.shouldBe(true)
                    response.body()?.id.shouldBe(3)
                }
            }
        }
        
        When("Se llama a updateGame() con un juego modificado") {
            val updatedGame = Game(
                id = 1,
                title = "Elden Ring - Enhanced Edition",
                price = 79.99,
                rating = 4.8f,
                image = "https://example.com/elden-ring.jpg"
            )
            
            coEvery { mockApiService.updateGame(1, updatedGame) } returns Response.success(updatedGame)
            
            Then("Debería actualizar el juego exitosamente") {
                runTest {
                    val response = mockApiService.updateGame(1, updatedGame)
                    response.isSuccessful.shouldBe(true)
                    response.body()?.price.shouldBe(79.99)
                    response.body()?.title.shouldBe("Elden Ring - Enhanced Edition")
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
        
        When("Se llama a patchGame() para actualización parcial") {
            val updates = mapOf("price" to 49.99, "discount" to 10.0)
            val patchedGame = Game(
                id = 1,
                title = "Elden Ring",
                price = 49.99,
                discount = 10.0,
                rating = 4.8f,
                image = "https://example.com/elden-ring.jpg"
            )
            
            coEvery { mockApiService.patchGame(1, updates) } returns Response.success(patchedGame)
            
            Then("Debería aplicar los cambios parciales") {
                runTest {
                    val response = mockApiService.patchGame(1, updates)
                    response.isSuccessful.shouldBe(true)
                    response.body()?.price.shouldBe(49.99)
                    response.body()?.discount.shouldBe(10.0)
                }
            }
        }
        
        When("La API retorna error") {
            coEvery { mockApiService.getGames() } returns Response.error(500, "Error interno del servidor".toResponseBody())
            
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
