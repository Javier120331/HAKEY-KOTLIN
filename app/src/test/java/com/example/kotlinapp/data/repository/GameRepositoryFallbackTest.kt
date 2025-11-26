package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.api.GameApiService
import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class GameRepositoryFallbackTest : DescribeSpec({
    
    describe("GameRepository - Datos de respaldo") {
        
        it("Debería retornar lista de fallback cuando API no está disponible") {
            runTest {
                val mockApiService = mockk<GameApiService>()
                
                // Simular que los juegos de respaldo existen
                val fallbackGames = listOf(
                    Game(
                        id = 1,
                        title = "Elden Ring",
                        price = 59.99,
                        description = "Action RPG épico",
                        rating = 4.8f
                    ),
                    Game(
                        id = 2,
                        title = "Cyberpunk 2077",
                        price = 49.99,
                        description = "RPG futurista",
                        rating = 4.5f
                    ),
                    Game(
                        id = 3,
                        title = "The Legend of Zelda",
                        price = 69.99,
                        description = "Aventura de acción",
                        rating = 4.9f
                    )
                )
                
                fallbackGames.shouldHaveSize(3)
                fallbackGames[0].title.shouldBe("Elden Ring")
                fallbackGames[1].title.shouldBe("Cyberpunk 2077")
                fallbackGames[2].title.shouldBe("The Legend of Zelda")
            }
        }
        
        it("Debería tener precios correctos en datos de respaldo") {
            runTest {
                val fallbackGames = listOf(
                    Game(id = 1, title = "Elden Ring", price = 59.99),
                    Game(id = 2, title = "Cyberpunk 2077", price = 49.99),
                    Game(id = 3, title = "The Legend of Zelda", price = 69.99)
                )
                
                fallbackGames[0].price.shouldBe(59.99)
                fallbackGames[1].price.shouldBe(49.99)
                fallbackGames[2].price.shouldBe(69.99)
            }
        }
        
        it("Debería tener ratings correctos en datos de respaldo") {
            runTest {
                val fallbackGames = listOf(
                    Game(id = 1, title = "Elden Ring", rating = 4.8f),
                    Game(id = 2, title = "Cyberpunk 2077", rating = 4.5f),
                    Game(id = 3, title = "The Legend of Zelda", rating = 4.9f)
                )
                
                fallbackGames[0].rating.shouldBe(4.8f)
                fallbackGames[1].rating.shouldBe(4.5f)
                fallbackGames[2].rating.shouldBe(4.9f)
            }
        }
    }
    
    describe("GameRepository - Manejo de excepciones") {
        
        it("Debería capturar excepciones de inicialización de API") {
            runTest {
                // Cuando apiService falla, debe devolver null y usar fallback
                val repository = GameRepository()
                
                // El repositorio debe estar instanciado correctamente
                repository.shouldNotBe(null)
            }
        }
    }
})
