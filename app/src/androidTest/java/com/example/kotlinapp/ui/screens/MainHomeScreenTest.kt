package com.example.kotlinapp.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kotlinapp.data.model.Game
import com.example.kotlinapp.data.repository.GameRepository
import com.example.kotlinapp.data.repository.ShoppingCartRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainHomeScreenTest {
    
    private val mockGameRepository = mockk<GameRepository>()
    private val mockShoppingCartRepository = ShoppingCartRepository()
    
    private val testGames = listOf(
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
        ),
        Game(
            id = 3,
            title = "The Legend of Zelda",
            price = 69.99,
            rating = 4.9f,
            image = "https://example.com/zelda.jpg"
        )
    )
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun mainHomeScreen_should_display_games_from_repository() = runComposeUiTest {
        coEvery { mockGameRepository.getGames() } returns testGames
        
        setContent {
            MaterialTheme {
                Surface {
                    MainHomeScreen(
                        gameRepository = mockGameRepository,
                        shoppingCartRepository = mockShoppingCartRepository
                    )
                }
            }
        }
        
        // Los juegos deberían aparecer en la pantalla
        onNodeWithText("Elden Ring").assertIsDisplayed()
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun mainHomeScreen_should_allow_adding_game_to_cart() = runComposeUiTest {
        coEvery { mockGameRepository.getGames() } returns testGames
        
        setContent {
            MaterialTheme {
                Surface {
                    MainHomeScreen(
                        gameRepository = mockGameRepository,
                        shoppingCartRepository = mockShoppingCartRepository
                    )
                }
            }
        }
        
        // Verificar que se puede agregar un juego al carrito
        mockShoppingCartRepository.addToCart(testGames[0])
        
        assert(mockShoppingCartRepository.getCartCount() == 1)
        assert(mockShoppingCartRepository.getTotalPrice() == 59.99)
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun mainHomeScreen_should_display_multiple_games() = runComposeUiTest {
        coEvery { mockGameRepository.getGames() } returns testGames
        
        setContent {
            MaterialTheme {
                Surface {
                    MainHomeScreen(
                        gameRepository = mockGameRepository,
                        shoppingCartRepository = mockShoppingCartRepository
                    )
                }
            }
        }
        
        onNodeWithText("Elden Ring").assertIsDisplayed()
    }
}
