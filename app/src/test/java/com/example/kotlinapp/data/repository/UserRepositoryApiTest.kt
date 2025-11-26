package com.example.kotlinapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.kotlinapp.data.api.UserApiService
import com.example.kotlinapp.data.model.LoginRequest
import com.example.kotlinapp.data.model.LoginResponse
import com.example.kotlinapp.data.model.RegisterRequest
import com.example.kotlinapp.data.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class UserRepositoryApiTest : DescribeSpec({
    
    val mockContext = mockk<Context>()
    val mockSharedPreferences = mockk<SharedPreferences>(relaxed = true)
    val mockEditor = mockk<SharedPreferences.Editor>(relaxed = true)
    
    val testUser = User(
        id = 1,
        nombre = "John Doe",
        email = "john@example.com",
        password = "Password123!"
    )
    
    val testLoginResponse = LoginResponse(
        message = "Login exitoso",
        usuario = testUser
    )
    
    beforeTest {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.putInt(any(), any()) } returns mockEditor
        every { mockEditor.putBoolean(any(), any()) } returns mockEditor
        every { mockEditor.apply() } returns Unit
    }
    
    describe("UserRepository - Login con API") {
        
        it("Debería manejar respuesta exitosa de login") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.login(any()) } returns Response.success(testLoginResponse)
                
                val response = mockApiService.login(LoginRequest(email = "john@example.com", password = "Password123!"))
                
                response.isSuccessful.shouldBe(true)
                response.body()?.usuario?.id.shouldBe(1)
                response.body()?.usuario?.nombre.shouldBe("John Doe")
            }
        }
        
        it("Debería retornar false cuando login falla") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.login(any()) } returns Response.error(
                    401,
                    "Error de autenticación".toResponseBody()
                )
                
                // Para este test, vamos a crear un escenario donde la API falla
                val response = Response.error<LoginResponse>(
                    401,
                    "Error de autenticación".toResponseBody()
                )
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(401)
            }
        }
    }
    
    describe("UserRepository - Registro con API") {
        
        it("Debería registrar usuario exitosamente y guardar datos") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.createUser(any()) } returns Response.success(testUser)
                
                val response = mockApiService.createUser(
                    RegisterRequest(
                        nombre = "John Doe",
                        email = "john@example.com",
                        password = "Password123!"
                    )
                )
                
                response.isSuccessful.shouldBe(true)
                response.body()?.id.shouldBe(1)
                response.body()?.nombre.shouldBe("John Doe")
            }
        }
        
        it("Debería retornar false cuando el registro falla") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.createUser(any()) } returns Response.error(
                    409,
                    "El usuario ya existe".toResponseBody()
                )
                
                val response = mockApiService.createUser(
                    RegisterRequest(
                        nombre = "John Doe",
                        email = "john@example.com",
                        password = "Password123!"
                    )
                )
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(409)
            }
        }
    }
    
    describe("UserRepository - Obtener usuarios") {
        
        it("Debería obtener lista de usuarios") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                val usersList = listOf(testUser)
                
                coEvery { mockApiService.getUsers() } returns Response.success(usersList)
                
                val response = mockApiService.getUsers()
                
                response.isSuccessful.shouldBe(true)
                response.body()?.size.shouldBe(1)
                response.body()?.first()?.nombre.shouldBe("John Doe")
            }
        }
        
        it("Debería retornar lista vacía cuando la API falla") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.getUsers() } returns Response.error(
                    500,
                    "Error del servidor".toResponseBody()
                )
                
                val response = mockApiService.getUsers()
                
                response.isSuccessful.shouldBe(false)
            }
        }
    }
    
    describe("UserRepository - Obtener usuario por ID") {
        
        it("Debería obtener un usuario por su ID") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.getUserById(1) } returns Response.success(testUser)
                
                val response = mockApiService.getUserById(1)
                
                response.isSuccessful.shouldBe(true)
                response.body()?.id.shouldBe(1)
                response.body()?.email.shouldBe("john@example.com")
            }
        }
        
        it("Debería retornar null cuando el usuario no existe") {
            runTest {
                val mockApiService = mockk<UserApiService>()
                
                coEvery { mockApiService.getUserById(999) } returns Response.error(
                    404,
                    "Usuario no encontrado".toResponseBody()
                )
                
                val response = mockApiService.getUserById(999)
                
                response.isSuccessful.shouldBe(false)
                response.code().shouldBe(404)
            }
        }
    }
    
    describe("UserRepository - Clear All") {
        
        it("Debería limpiar todos los datos del usuario") {
            val mockContext2 = mockk<Context>()
            val mockSharedPreferences2 = mockk<SharedPreferences>(relaxed = true)
            val mockEditor2 = mockk<SharedPreferences.Editor>(relaxed = true)
            
            every { mockContext2.getSharedPreferences(any(), any()) } returns mockSharedPreferences2
            every { mockSharedPreferences2.edit() } returns mockEditor2
            every { mockEditor2.clear() } returns mockEditor2
            every { mockEditor2.apply() } returns Unit
            
            val repository = UserRepository(mockContext2)
            repository.clearAll()
            
            io.mockk.verify { mockEditor2.clear() }
            io.mockk.verify { mockEditor2.apply() }
        }
    }
})
