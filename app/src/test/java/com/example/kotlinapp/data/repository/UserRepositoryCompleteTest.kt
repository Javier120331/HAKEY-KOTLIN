package com.example.kotlinapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.kotlinapp.data.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserRepositoryCompleteTest : DescribeSpec({
    
    val mockContext = mockk<Context>()
    val mockSharedPreferences = mockk<SharedPreferences>(relaxed = true)
    val mockEditor = mockk<SharedPreferences.Editor>(relaxed = true)
    
    beforeTest {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.putInt(any(), any()) } returns mockEditor
        every { mockEditor.putBoolean(any(), any()) } returns mockEditor
        every { mockEditor.clear() } returns mockEditor
        every { mockEditor.apply() } returns Unit
    }
    
    describe("UserRepository - Obtener datos complejos") {
        
        it("Debería obtener email guardado") {
            every { mockSharedPreferences.getString("user_email", null) } returns "test@example.com"
            
            val repository = UserRepository(mockContext)
            val email = repository.getUserEmail()
            
            email.shouldBe("test@example.com")
        }
        
        it("Debería retornar null si email no existe") {
            every { mockSharedPreferences.getString("user_email", null) } returns null
            
            val repository = UserRepository(mockContext)
            val email = repository.getUserEmail()
            
            email.shouldBe(null)
        }
        
        it("Debería obtener nombre de usuario guardado") {
            every { mockSharedPreferences.getString("user_name", null) } returns "Juan Pérez"
            
            val repository = UserRepository(mockContext)
            val name = repository.getUserName()
            
            name.shouldBe("Juan Pérez")
        }
        
        it("Debería obtener display name") {
            every { mockSharedPreferences.getString("display_name", null) } returns "Juan"
            
            val repository = UserRepository(mockContext)
            val displayName = repository.getDisplayName()
            
            displayName.shouldBe("Juan")
        }
        
        it("Debería obtener URL de perfil") {
            every { mockSharedPreferences.getString("profile_image_url", null) } returns "https://example.com/profile.jpg"
            
            val repository = UserRepository(mockContext)
            val imageUrl = repository.getProfileImageUrl()
            
            imageUrl.shouldBe("https://example.com/profile.jpg")
        }
    }
    
    describe("UserRepository - Actualizar datos guardados") {
        
        it("Debería actualizar display name y verificar apply") {
            val repository = UserRepository(mockContext)
            repository.setDisplayName("NuevoNombre")
            
            verify { mockEditor.putString("display_name", "NuevoNombre") }
            verify { mockEditor.apply() }
        }
        
        it("Debería actualizar URL de perfil") {
            val repository = UserRepository(mockContext)
            repository.setProfileImageUrl("https://new-url.com/profile.jpg")
            
            verify { mockEditor.putString("profile_image_url", "https://new-url.com/profile.jpg") }
            verify { mockEditor.apply() }
        }
        
        it("Debería actualizar múltiples valores en secuencia") {
            val repository = UserRepository(mockContext)
            
            repository.setDisplayName("Nombre1")
            repository.setProfileImageUrl("https://url1.com")
            repository.setDisplayName("Nombre2")
            
            verify(atLeast = 1) { mockEditor.putString(any(), any()) }
            verify(atLeast = 3) { mockEditor.apply() }
        }
    }
    
    describe("UserRepository - Logout y limpieza") {
        
        it("Debería establecer is_logged_in en false") {
            val repository = UserRepository(mockContext)
            repository.logout()
            
            verify { mockEditor.putBoolean("is_logged_in", false) }
            verify { mockEditor.apply() }
        }
        
        it("Debería limpiar todos los datos") {
            val repository = UserRepository(mockContext)
            repository.clearAll()
            
            verify { mockEditor.clear() }
            verify { mockEditor.apply() }
        }
        
        it("Debería permitir múltiples logouts consecutivos") {
            val repository = UserRepository(mockContext)
            
            repository.logout()
            repository.logout()
            
            verify(atLeast = 2) { mockEditor.putBoolean("is_logged_in", false) }
        }
    }
    
    describe("UserRepository - Estado de sesión") {
        
        it("Debería verificar si usuario está logueado") {
            every { mockSharedPreferences.getBoolean("is_logged_in", false) } returns true
            
            val repository = UserRepository(mockContext)
            val isLoggedIn = repository.isLoggedIn()
            
            isLoggedIn.shouldBe(true)
        }
        
        it("Debería retornar false cuando no está logueado") {
            every { mockSharedPreferences.getBoolean("is_logged_in", false) } returns false
            
            val repository = UserRepository(mockContext)
            val isLoggedIn = repository.isLoggedIn()
            
            isLoggedIn.shouldBe(false)
        }
        
        it("Debería obtener ID del usuario") {
            every { mockSharedPreferences.getInt("user_id", 0) } returns 42
            
            val repository = UserRepository(mockContext)
            val userId = repository.getUserId()
            
            userId.shouldBe(42)
        }
        
        it("Debería retornar 0 cuando no hay usuario logueado") {
            every { mockSharedPreferences.getInt("user_id", 0) } returns 0
            
            val repository = UserRepository(mockContext)
            val userId = repository.getUserId()
            
            userId.shouldBe(0)
        }
    }
    
    describe("UserRepository - Manejo de datos nulos") {
        
        it("Debería manejar email nulo correctamente") {
            every { mockSharedPreferences.getString("user_email", null) } returns null
            
            val repository = UserRepository(mockContext)
            val email = repository.getUserEmail()
            
            email.shouldBe(null)
        }
        
        it("Debería manejar nombre nulo") {
            every { mockSharedPreferences.getString("user_name", null) } returns null
            
            val repository = UserRepository(mockContext)
            val name = repository.getUserName()
            
            name.shouldBe(null)
        }
        
        it("Debería manejar display name nulo") {
            every { mockSharedPreferences.getString("display_name", null) } returns null
            
            val repository = UserRepository(mockContext)
            val displayName = repository.getDisplayName()
            
            displayName.shouldBe(null)
        }
    }
})
