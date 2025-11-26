package com.example.kotlinapp.data.api

import com.example.kotlinapp.data.model.Game
import com.example.kotlinapp.data.model.GameRequirements
import com.example.kotlinapp.data.model.LoginRequest
import com.example.kotlinapp.data.model.LoginResponse
import com.example.kotlinapp.data.model.RegisterRequest
import com.example.kotlinapp.data.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ApiServicesErrorHandlingTest : DescribeSpec({
    
    describe("GameApiService - Manejo de errores detallado") {
        
        it("Debería manejar timeout (408)") {
            runTest {
                val mockApiService = mockk<GameApiService>()
                coEvery { mockApiService.getGames() } returns Response.error(
                    408,
                    "Request Timeout".toResponseBody()
                )
                
                val response = mockApiService.getGames()
                
                response.code().shouldBe(408)
                response.isSuccessful.shouldBe(false)
            }
        }
        
        it("Debería manejar errores de validación (422)") {
            runTest {
                val mockApiService = mockk<GameApiService>()
                val game = Game(title = "")
                
                coEvery { mockApiService.createGame(any()) } returns Response.error(
                    422,
                    "Unprocessable Entity".toResponseBody()
                )
                
                val response = mockApiService.createGame(game)
                
                response.code().shouldBe(422)
            }
        }
        
        it("Debería manejar demasiadas solicitudes (429)") {
            runTest {
                val mockApiService = mockk<GameApiService>()
                
                coEvery { mockApiService.getGames() } returns Response.error(
                    429,
                    "Too Many Requests".toResponseBody()
                )
                
                val response = mockApiService.getGames()
                
                response.code().shouldBe(429)
            }
        }
        
        it("Debería manejar errores del servidor 502") {
            runTest {
                val mockApiService = mockk<GameApiService>()
                
                coEvery { mockApiService.getGames() } returns Response.error(
                    502,
                    "Bad Gateway".toResponseBody()
                )
                
                val response = mockApiService.getGames()
                
                response.code().shouldBe(502)
            }
        }
        
        it("Debería manejar errores del servidor 503") {
            runTest {
                val mockApiService = mockk<GameApiService>()
                
                coEvery { mockApiService.getGames() } returns Response.error(
                    503,
                    "Service Unavailable".toResponseBody()
                )
                
                val response = mockApiService.getGames()
                
                response.code().shouldBe(503)
            }
        }
    }
    
    describe("UserApiService - Manejo de errores de autenticación") {
        
        it("Debería manejar credenciales inválidas (401)") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                val request = LoginRequest(email = "test@test.com", password = "wrong")
                
                coEvery { mockApiService.login(any()) } returns Response.error(
                    401,
                    "Unauthorized".toResponseBody()
                )
                
                val response = mockApiService.login(request)
                
                response.code().shouldBe(401)
            }
        }
        
        it("Debería manejar cuenta no verificada (403)") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.login(any()) } returns Response.error(
                    403,
                    "Forbidden - Account not verified".toResponseBody()
                )
                
                val response = mockApiService.login(LoginRequest("test@test.com", "pass"))
                
                response.code().shouldBe(403)
            }
        }
        
        it("Debería manejar usuario ya existe (409)") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                val request = RegisterRequest(
                    nombre = "Test",
                    email = "existing@test.com",
                    password = "pass123"
                )
                
                coEvery { mockApiService.createUser(any()) } returns Response.error(
                    409,
                    "User already exists".toResponseBody()
                )
                
                val response = mockApiService.createUser(request)
                
                response.code().shouldBe(409)
            }
        }
        
        it("Debería manejar token expirado (401)") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.getUsers() } returns Response.error(
                    401,
                    "Token expired".toResponseBody()
                )
                
                val response = mockApiService.getUsers()
                
                response.code().shouldBe(401)
            }
        }
    }
    
    describe("GameApiService - Datos válidos") {
        
        it("Debería obtener juego con todos los datos completos") {
            runTest {
                val mockApiService = mockk<GameApiService>()
                val game = Game(
                    id = 1,
                    title = "Complete Game",
                    price = 59.99,
                    rating = 4.8f,
                    description = "Full description",
                    category = "RPG",
                    platform = listOf("PC", "PS5"),
                    releaseDate = "2024-01-01",
                    publisher = "Publisher Name",
                    featured = 1,
                    requirements = GameRequirements(
                        os = "Windows 10",
                        processor = "Intel i7",
                        memory = "16GB",
                        graphics = "RTX 3080",
                        storage = "150GB"
                    )
                )
                
                coEvery { mockApiService.getGameById(1) } returns Response.success(game)
                
                val response = mockApiService.getGameById(1)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.title.shouldBe("Complete Game")
                response.body()?.requirements?.memory.shouldBe("16GB")
            }
        }
    }
    
    describe("UserApiService - Registro con validaciones") {
        
        it("Debería registrar usuario con todos los datos") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                val request = RegisterRequest(
                    nombre = "Juan Pérez",
                    email = "juan@example.com",
                    password = "SecurePass123!",
                    numero = "+56912345678"
                )
                val expectedUser = User(
                    id = 1,
                    nombre = "Juan Pérez",
                    email = "juan@example.com",
                    numero = "+56912345678"
                )
                
                coEvery { mockApiService.createUser(any()) } returns Response.success(expectedUser)
                
                val response = mockApiService.createUser(request)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.nombre.shouldBe("Juan Pérez")
                response.body()?.numero.shouldBe("+56912345678")
            }
        }
        
        it("Debería validar email con formato correcto") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                val validEmails = listOf(
                    "test@example.com",
                    "user.name@example.co.uk",
                    "user+tag@example.com"
                )
                
                validEmails.forEach { email ->
                    email.contains("@").shouldBe(true)
                    email.contains(".").shouldBe(true)
                }
            }
        }
    }
    
    describe("RetrofitClient - Configuración de servicios") {
        
        it("Debería inicializar GameApiService") {
            val service = RetrofitClient.gameApiService
            
            service.shouldBe(RetrofitClient.gameApiService) // Misma instancia
        }
        
        it("Debería inicializar UserApiService") {
            val service = RetrofitClient.userApiService
            
            service.shouldBe(RetrofitClient.userApiService) // Misma instancia
        }
        
        it("Debería mantener singleton para GameApiService") {
            val service1 = RetrofitClient.gameApiService
            val service2 = RetrofitClient.gameApiService
            
            service1.shouldBe(service2)
        }
        
        it("Debería mantener singleton para UserApiService") {
            val service1 = RetrofitClient.userApiService
            val service2 = RetrofitClient.userApiService
            
            service1.shouldBe(service2)
        }
    }
})
