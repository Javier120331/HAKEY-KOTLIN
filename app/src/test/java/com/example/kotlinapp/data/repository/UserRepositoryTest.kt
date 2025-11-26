package com.example.kotlinapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.kotlinapp.data.model.LoginRequest
import com.example.kotlinapp.data.model.LoginResponse
import com.example.kotlinapp.data.model.RegisterRequest
import com.example.kotlinapp.data.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest

class UserRepositoryTest : DescribeSpec({
    
    val mockContext = mockk<Context>()
    val mockSharedPreferences = mockk<SharedPreferences>(relaxed = true)
    val mockEditor = mockk<SharedPreferences.Editor>(relaxed = true)
    
    val testUser = User(
        id = 1,
        nombre = "John Doe",
        email = "john@example.com",
        password = "Password123!"
    )
    
    beforeTest {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.putBoolean(any(), any()) } returns mockEditor
        every { mockEditor.apply() } returns Unit
    }
    
    describe("UserRepository - Gestión de sesión") {
        
        it("Debería verificar si el usuario está logueado") {
            every { mockSharedPreferences.getBoolean("is_logged_in", false) } returns true
            
            val repository = UserRepository(mockContext)
            val isLoggedIn = repository.isLoggedIn()
            
            isLoggedIn.shouldBe(true)
        }
        
        it("Debería retornar false cuando el usuario no está logueado") {
            every { mockSharedPreferences.getBoolean("is_logged_in", false) } returns false
            
            val repository = UserRepository(mockContext)
            val isLoggedIn = repository.isLoggedIn()
            
            isLoggedIn.shouldBe(false)
        }
        
        it("Debería obtener el ID del usuario logueado") {
            every { mockSharedPreferences.getInt("user_id", 0) } returns 1
            
            val repository = UserRepository(mockContext)
            val userId = repository.getUserId()
            
            userId.shouldBe(1)
        }
        
        it("Debería retornar 0 si no hay usuario logueado") {
            every { mockSharedPreferences.getInt("user_id", 0) } returns 0
            
            val repository = UserRepository(mockContext)
            val userId = repository.getUserId()
            
            userId.shouldBe(0)
        }
    }
    
    describe("UserRepository - Obtener datos del usuario") {
        
        it("Debería obtener el email del usuario") {
            every { mockSharedPreferences.getString("user_email", null) } returns "john@example.com"
            
            val repository = UserRepository(mockContext)
            val email = repository.getUserEmail()
            
            email.shouldBe("john@example.com")
        }
        
        it("Debería retornar null si no hay email guardado") {
            every { mockSharedPreferences.getString("user_email", null) } returns null
            
            val repository = UserRepository(mockContext)
            val email = repository.getUserEmail()
            
            email.shouldBe(null)
        }
        
        it("Debería obtener el nombre del usuario") {
            every { mockSharedPreferences.getString("user_name", null) } returns "John Doe"
            
            val repository = UserRepository(mockContext)
            val name = repository.getUserName()
            
            name.shouldBe("John Doe")
        }
        
        it("Debería obtener el display name del usuario") {
            every { mockSharedPreferences.getString("display_name", null) } returns "John"
            
            val repository = UserRepository(mockContext)
            val displayName = repository.getDisplayName()
            
            displayName.shouldBe("John")
        }
    }
    
    describe("UserRepository - Actualizar datos del usuario") {
        
        it("Debería actualizar el display name del usuario") {
            val repository = UserRepository(mockContext)
            repository.setDisplayName("Johnny")
            
            verify { mockEditor.putString("display_name", "Johnny") }
            verify { mockEditor.apply() }
        }
        
        it("Debería obtener la URL de la foto de perfil") {
            every { mockSharedPreferences.getString("profile_image_url", null) } returns "https://example.com/profile.jpg"
            
            val repository = UserRepository(mockContext)
            val imageUrl = repository.getProfileImageUrl()
            
            imageUrl.shouldBe("https://example.com/profile.jpg")
        }
        
        it("Debería actualizar la URL de la foto de perfil") {
            val repository = UserRepository(mockContext)
            repository.setProfileImageUrl("https://example.com/new-profile.jpg")
            
            verify { mockEditor.putString("profile_image_url", "https://example.com/new-profile.jpg") }
            verify { mockEditor.apply() }
        }
    }
    
    describe("UserRepository - Logout") {
        
        it("Debería limpiar la sesión del usuario") {
            val repository = UserRepository(mockContext)
            repository.logout()
            
            verify { mockEditor.putBoolean("is_logged_in", false) }
            verify { mockEditor.apply() }
        }
    }
})
