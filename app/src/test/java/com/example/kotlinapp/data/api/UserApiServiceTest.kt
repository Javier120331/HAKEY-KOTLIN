package com.example.kotlinapp.data.api

import com.example.kotlinapp.data.model.LoginRequest
import com.example.kotlinapp.data.model.LoginResponse
import com.example.kotlinapp.data.model.RegisterRequest
import com.example.kotlinapp.data.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class UserApiServiceTest : DescribeSpec({
    val mockApiService = mockk<UserApiService>()
    
    val testUser = User(
        id = 1,
        nombre = "testuser",
        email = "test@example.com"
    )
    
    describe("UserApiService - Obtener usuarios") {
        
        it("Debería obtener la lista de usuarios") {
            val mockUsers = listOf(
                testUser,
                User(
                    id = 2,
                    nombre = "otheruser",
                    email = "other@example.com"
                )
            )
            
            coEvery { mockApiService.getUsers() } returns Response.success(mockUsers)
            
            runTest {
                val response = mockApiService.getUsers()
                response.isSuccessful.shouldBe(true)
                response.body()?.size.shouldBe(2)
            }
        }
        
        it("Debería obtener un usuario por ID") {
            coEvery { mockApiService.getUserById(1) } returns Response.success(testUser)
            
            runTest {
                val response = mockApiService.getUserById(1)
                response.isSuccessful.shouldBe(true)
                response.body()?.id.shouldBe(1)
                response.body()?.nombre.shouldBe("testuser")
            }
        }
    }
    
    describe("UserApiService - Crear usuarios") {
        
        it("Debería crear un nuevo usuario con registro") {
            val registerRequest = RegisterRequest(
                nombre = "newuser",
                email = "new@example.com",
                password = "SecurePass123!"
            )
            
            val createdUser = User(
                id = 3,
                nombre = "newuser",
                email = "new@example.com"
            )
            
            coEvery { mockApiService.createUser(registerRequest) } returns Response.success(createdUser)
            
            runTest {
                val response = mockApiService.createUser(registerRequest)
                response.isSuccessful.shouldBe(true)
                response.body()?.id.shouldBe(3)
            }
        }
        
        it("Debería autenticar un usuario con login") {
            val loginRequest = LoginRequest(
                email = "test@example.com",
                password = "SecurePass123!"
            )
            
            val loginResponse = LoginResponse(
                message = "Login exitoso",
                usuario = testUser
            )
            
            coEvery { mockApiService.login(loginRequest) } returns Response.success(loginResponse)
            
            runTest {
                val response = mockApiService.login(loginRequest)
                response.isSuccessful.shouldBe(true)
                response.body()?.message.shouldBe("Login exitoso")
                response.body()?.usuario?.id.shouldBe(1)
            }
        }
    }
    
    describe("UserApiService - Actualizar usuarios") {
        
        it("Debería actualizar un usuario completamente") {
            val updatedUser = testUser.copy(email = "newemail@example.com")
            
            coEvery { mockApiService.updateUser(1, updatedUser) } returns Response.success(updatedUser)
            
            runTest {
                val response = mockApiService.updateUser(1, updatedUser)
                response.isSuccessful.shouldBe(true)
                response.body()?.email.shouldBe("newemail@example.com")
            }
        }
        
        it("Debería aplicar cambios parciales al usuario") {
            val updates = mapOf("email" to "updated@example.com")
            
            val patchedUser = testUser.copy(email = "updated@example.com")
            
            coEvery { mockApiService.patchUser(1, updates) } returns Response.success(patchedUser)
            
            runTest {
                val response = mockApiService.patchUser(1, updates)
                response.isSuccessful.shouldBe(true)
                response.body()?.email.shouldBe("updated@example.com")
            }
        }
    }
    
    describe("UserApiService - Eliminar usuarios") {
        
        it("Debería eliminar un usuario por ID") {
            coEvery { mockApiService.deleteUser(1) } returns Response.success(Unit)
            
            runTest {
                val response = mockApiService.deleteUser(1)
                response.isSuccessful.shouldBe(true)
            }
        }
    }
    
    describe("UserApiService - Manejo de errores") {
        
        it("Debería retornar error en login con credenciales inválidas") {
            val loginRequest = LoginRequest(
                email = "test@example.com",
                password = "WrongPassword"
            )
            
            coEvery { mockApiService.login(loginRequest) } returns Response.error(401, "Credenciales inválidas".toResponseBody())
            
            runTest {
                val response = mockApiService.login(loginRequest)
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(401)
            }
        }
        
        it("Debería retornar error en solicitud no encontrada") {
            coEvery { mockApiService.getUserById(999) } returns Response.error(404, "No encontrado".toResponseBody())
            
            runTest {
                val response = mockApiService.getUserById(999)
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(404)
            }
        }
    }
})
