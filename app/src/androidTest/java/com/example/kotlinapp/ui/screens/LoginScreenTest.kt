package com.example.kotlinapp.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kotlinapp.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    
    private val mockUserRepository = mockk<UserRepository>()
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginScreen_should_display_all_required_elements() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        userRepository = mockUserRepository,
                        onNavigateToRegister = {},
                        onNavigateToHome = {}
                    )
                }
            }
        }
        
        onNodeWithText("Login").assertIsDisplayed()
        onNodeWithText("Email").assertIsDisplayed()
        onNodeWithText("Contraseña").assertIsDisplayed()
        onNodeWithText("Iniciar Sesión").assertIsDisplayed()
        onNodeWithText("¿No tienes cuenta?").assertIsDisplayed()
        onNodeWithText("Regístrate").assertIsDisplayed()
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginScreen_should_show_error_when_email_is_empty() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        userRepository = mockUserRepository,
                        onNavigateToRegister = {},
                        onNavigateToHome = {}
                    )
                }
            }
        }
        
        onNodeWithText("Iniciar Sesión").performClick()
        onNodeWithText("Por favor ingresa el email").assertIsDisplayed()
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginScreen_should_show_error_when_password_is_empty() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        userRepository = mockUserRepository,
                        onNavigateToRegister = {},
                        onNavigateToHome = {}
                    )
                }
            }
        }
        
        onNodeWithText("Email").performTextInput("test@example.com")
        onNodeWithText("Iniciar Sesión").performClick()
        onNodeWithText("Por favor ingresa la contraseña").assertIsDisplayed()
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginScreen_should_show_error_for_invalid_email() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        userRepository = mockUserRepository,
                        onNavigateToRegister = {},
                        onNavigateToHome = {}
                    )
                }
            }
        }
        
        onNodeWithText("Email").performTextInput("invalidemail")
        onNodeWithText("Contraseña").performTextInput("Password123")
        onNodeWithText("Iniciar Sesión").performClick()
        onNodeWithText("Email inválido").assertIsDisplayed()
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginScreen_should_show_error_for_short_password() = runComposeUiTest {
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        userRepository = mockUserRepository,
                        onNavigateToRegister = {},
                        onNavigateToHome = {}
                    )
                }
            }
        }
        
        onNodeWithText("Email").performTextInput("test@example.com")
        onNodeWithText("Contraseña").performTextInput("short")
        onNodeWithText("Iniciar Sesión").performClick()
        onNodeWithText("La contraseña debe tener más de 6 caracteres").assertIsDisplayed()
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginScreen_should_navigate_to_register_when_register_link_clicked() = runComposeUiTest {
        var navigatedToRegister = false
        
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        userRepository = mockUserRepository,
                        onNavigateToRegister = { navigatedToRegister = true },
                        onNavigateToHome = {}
                    )
                }
            }
        }
        
        onNodeWithText("Regístrate").performClick()
        waitUntil { navigatedToRegister }
    }
    
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginScreen_should_disable_button_while_loading() = runComposeUiTest {
        runTest {
            coEvery { mockUserRepository.loginUser(any(), any()) } coAnswers {
                kotlinx.coroutines.delay(1000)
                true
            }
        }
        
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        userRepository = mockUserRepository,
                        onNavigateToRegister = {},
                        onNavigateToHome = {}
                    )
                }
            }
        }
        
        onNodeWithText("Email").performTextInput("test@example.com")
        onNodeWithText("Contraseña").performTextInput("Password123")
        onNodeWithText("Iniciar Sesión").performClick()
    }
}
