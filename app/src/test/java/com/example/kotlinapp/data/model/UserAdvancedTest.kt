package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class UserAdvancedTest : DescribeSpec({
    
    describe("User - Validación de datos") {
        
        it("Debería crear usuario con valores mínimos") {
            val user = User(
                id = 1,
                nombre = "John",
                email = "john@example.com",
                password = "pass123"
            )
            
            user.id.shouldBe(1)
            user.nombre.shouldBe("John")
            user.email.shouldBe("john@example.com")
        }
        
        it("Debería permitir usuario sin número de teléfono") {
            val user = User(
                id = 1,
                nombre = "John",
                email = "john@example.com",
                password = "pass123"
            )
            
            user.numero.shouldBe(null)
        }
        
        it("Debería permitir usuario con número de teléfono") {
            val user = User(
                id = 1,
                nombre = "John",
                email = "john@example.com",
                password = "pass123",
                numero = "555-1234"
            )
            
            user.numero.shouldBe("555-1234")
        }
    }
    
    describe("User - Comparación de datos") {
        
        it("Debería permitir copiar usuario con cambios") {
            val user1 = User(
                id = 1,
                nombre = "John",
                email = "john@example.com",
                password = "pass123"
            )
            
            val user2 = user1.copy(nombre = "Jane")
            
            user1.nombre.shouldBe("John")
            user2.nombre.shouldBe("Jane")
            user2.id.shouldBe(1)
        }
    }
})

class LoginRequestAdvancedTest : DescribeSpec({
    
    describe("LoginRequest - Validación") {
        
        it("Debería crear LoginRequest con email y password") {
            val request = LoginRequest(
                email = "user@example.com",
                password = "SecurePass123!"
            )
            
            request.email.shouldBe("user@example.com")
            request.password.shouldBe("SecurePass123!")
        }
        
        it("Debería permitir email vacío") {
            val request = LoginRequest(
                email = "",
                password = "password"
            )
            
            request.email.shouldBe("")
        }
        
        it("Debería permitir password vacío") {
            val request = LoginRequest(
                email = "user@example.com",
                password = ""
            )
            
            request.password.shouldBe("")
        }
    }
    
    describe("LoginRequest - Comparación") {
        
        it("Debería permitir copiar LoginRequest con cambios") {
            val request1 = LoginRequest(
                email = "old@example.com",
                password = "pass123"
            )
            
            val request2 = request1.copy(email = "new@example.com")
            
            request1.email.shouldBe("old@example.com")
            request2.email.shouldBe("new@example.com")
            request2.password.shouldBe("pass123")
        }
    }
})

class LoginResponseAdvancedTest : DescribeSpec({
    
    val testUser = User(
        id = 1,
        nombre = "John Doe",
        email = "john@example.com",
        password = "Password123!"
    )
    
    describe("LoginResponse - Propiedades") {
        
        it("Debería contener mensaje de respuesta") {
            val response = LoginResponse(
                message = "Login exitoso",
                usuario = testUser
            )
            
            response.message.shouldBe("Login exitoso")
        }
        
        it("Debería contener datos del usuario") {
            val response = LoginResponse(
                message = "Login exitoso",
                usuario = testUser
            )
            
            response.usuario.id.shouldBe(1)
            response.usuario.nombre.shouldBe("John Doe")
            response.usuario.email.shouldBe("john@example.com")
        }
    }
    
    describe("LoginResponse - Diferentes mensajes") {
        
        it("Debería manejar mensaje de error") {
            val response = LoginResponse(
                message = "Email o contraseña incorrectos",
                usuario = testUser
            )
            
            response.message.shouldBe("Email o contraseña incorrectos")
        }
        
        it("Debería manejar mensaje vacío") {
            val response = LoginResponse(
                message = "",
                usuario = testUser
            )
            
            response.message.shouldBe("")
        }
    }
})

class RegisterRequestAdvancedTest : DescribeSpec({
    
    describe("RegisterRequest - Campos requeridos") {
        
        it("Debería crear RegisterRequest con campos obligatorios") {
            val request = RegisterRequest(
                nombre = "John Doe",
                email = "john@example.com",
                password = "SecurePass123!"
            )
            
            request.nombre.shouldBe("John Doe")
            request.email.shouldBe("john@example.com")
            request.password.shouldBe("SecurePass123!")
            request.numero.shouldBe(null)
        }
        
        it("Debería crear RegisterRequest con número opcional") {
            val request = RegisterRequest(
                nombre = "John Doe",
                email = "john@example.com",
                password = "SecurePass123!",
                numero = "555-1234"
            )
            
            request.numero.shouldBe("555-1234")
        }
    }
    
    describe("RegisterRequest - Validación de datos") {
        
        it("Debería permitir nombre largo") {
            val request = RegisterRequest(
                nombre = "John Michael Christopher Alexander Doe",
                email = "john@example.com",
                password = "pass"
            )
            
            request.nombre.length > 10
        }
        
        it("Debería permitir emails con diferentes formatos") {
            val request1 = RegisterRequest(
                nombre = "John",
                email = "john.doe@example.co.uk",
                password = "pass"
            )
            
            val request2 = RegisterRequest(
                nombre = "John",
                email = "john+test@example.com",
                password = "pass"
            )
            
            request1.email.shouldBe("john.doe@example.co.uk")
            request2.email.shouldBe("john+test@example.com")
        }
    }
    
    describe("RegisterRequest - Copiar con cambios") {
        
        it("Debería permitir copiar RegisterRequest") {
            val request1 = RegisterRequest(
                nombre = "John",
                email = "john@example.com",
                password = "pass123"
            )
            
            val request2 = request1.copy(
                nombre = "Jane",
                email = "jane@example.com"
            )
            
            request1.nombre.shouldBe("John")
            request2.nombre.shouldBe("Jane")
            request2.password.shouldBe("pass123")
        }
    }
})
