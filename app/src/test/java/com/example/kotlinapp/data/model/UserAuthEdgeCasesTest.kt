package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UserEdgeCasesTest : DescribeSpec({
    
    describe("User - Casos límite") {
        
        it("Debería manejar usuario con ID 0") {
            val user = User(id = 0, nombre = "Test", email = "test@test.com")
            
            user.id.shouldBe(0)
        }
        
        it("Debería manejar usuario con nombre vacío") {
            val user = User(id = 1, nombre = "", email = "test@test.com")
            
            user.nombre.shouldBe("")
            user.nombre.isEmpty().shouldBe(true)
        }
        
        it("Debería manejar usuario con nombre muy largo") {
            val longName = "A".repeat(200)
            val user = User(id = 1, nombre = longName, email = "test@test.com")
            
            user.nombre.shouldBe(longName)
        }
        
        it("Debería manejar usuario con email vacío") {
            val user = User(id = 1, nombre = "Test", email = "")
            
            user.email.shouldBe("")
        }
        
        it("Debería manejar usuario con email muy largo") {
            val longEmail = "a".repeat(50) + "@example.com"
            val user = User(id = 1, nombre = "Test", email = longEmail)
            
            user.email.shouldBe(longEmail)
        }
        
        it("Debería manejar usuario con password vacío") {
            val user = User(id = 1, nombre = "Test", email = "test@test.com", password = "")
            
            user.password.shouldBe("")
        }
        
        it("Debería manejar usuario con número nulo") {
            val user = User(id = 1, nombre = "Test", email = "test@test.com", numero = null)
            
            user.numero.shouldBe(null)
        }
        
        it("Debería manejar usuario con número vacío") {
            val user = User(id = 1, nombre = "Test", email = "test@test.com", numero = "")
            
            user.numero.shouldBe("")
        }
        
        it("Debería permitir copiar usuario con cambios") {
            val user1 = User(id = 1, nombre = "John", email = "john@test.com", password = "Pass123")
            val user2 = user1.copy(nombre = "Jane")
            
            user1.nombre.shouldBe("John")
            user2.nombre.shouldBe("Jane")
            user2.email.shouldBe("john@test.com")
        }
        
        it("Debería detectar usuarios iguales") {
            val user1 = User(id = 1, nombre = "Test", email = "test@test.com")
            val user2 = User(id = 1, nombre = "Test", email = "test@test.com")
            
            user1.shouldBe(user2)
        }
        
        it("Debería detectar usuarios diferentes") {
            val user1 = User(id = 1, nombre = "Test1", email = "test1@test.com")
            val user2 = User(id = 2, nombre = "Test2", email = "test2@test.com")
            
            (user1 != user2).shouldBe(true)
        }
    }
})

class LoginRequestEdgeCasesTest : DescribeSpec({
    
    describe("LoginRequest - Casos límite") {
        
        it("Debería crear LoginRequest con email vacío") {
            val request = LoginRequest(email = "", password = "Pass123")
            
            request.email.shouldBe("")
        }
        
        it("Debería crear LoginRequest con password vacío") {
            val request = LoginRequest(email = "test@test.com", password = "")
            
            request.password.shouldBe("")
        }
        
        it("Debería crear LoginRequest con valores normales") {
            val request = LoginRequest(email = "user@example.com", password = "SecurePass123!")
            
            request.email.shouldBe("user@example.com")
            request.password.shouldBe("SecurePass123!")
        }
        
        it("Debería detectar LoginRequest iguales") {
            val request1 = LoginRequest(email = "test@test.com", password = "Pass123")
            val request2 = LoginRequest(email = "test@test.com", password = "Pass123")
            
            request1.shouldBe(request2)
        }
    }
})

class RegisterRequestEdgeCasesTest : DescribeSpec({
    
    describe("RegisterRequest - Casos límite") {
        
        it("Debería crear RegisterRequest sin número (null)") {
            val request = RegisterRequest(
                nombre = "John",
                email = "john@test.com",
                password = "Pass123",
                numero = null
            )
            
            request.numero.shouldBe(null)
        }
        
        it("Debería crear RegisterRequest con número") {
            val request = RegisterRequest(
                nombre = "John",
                email = "john@test.com",
                password = "Pass123",
                numero = "555-1234"
            )
            
            request.numero.shouldBe("555-1234")
        }
        
        it("Debería manejar RegisterRequest con nombre vacío") {
            val request = RegisterRequest(
                nombre = "",
                email = "test@test.com",
                password = "Pass123"
            )
            
            request.nombre.shouldBe("")
        }
        
        it("Debería manejar RegisterRequest con email vacío") {
            val request = RegisterRequest(
                nombre = "Test",
                email = "",
                password = "Pass123"
            )
            
            request.email.shouldBe("")
        }
        
        it("Debería manejar RegisterRequest con password vacío") {
            val request = RegisterRequest(
                nombre = "Test",
                email = "test@test.com",
                password = ""
            )
            
            request.password.shouldBe("")
        }
        
        it("Debería detectar RegisterRequest iguales") {
            val request1 = RegisterRequest(
                nombre = "John",
                email = "john@test.com",
                password = "Pass123",
                numero = "555-1234"
            )
            val request2 = RegisterRequest(
                nombre = "John",
                email = "john@test.com",
                password = "Pass123",
                numero = "555-1234"
            )
            
            request1.shouldBe(request2)
        }
    }
})

class LoginResponseEdgeCasesTest : DescribeSpec({
    
    describe("LoginResponse - Casos límite") {
        
        it("Debería crear LoginResponse con mensaje") {
            val user = User(id = 1, nombre = "John", email = "john@test.com")
            val response = LoginResponse(
                message = "Login exitoso",
                usuario = user
            )
            
            response.message.shouldBe("Login exitoso")
            response.usuario.shouldBe(user)
        }
        
        it("Debería manejar LoginResponse con mensaje vacío") {
            val user = User(id = 1, nombre = "John", email = "john@test.com")
            val response = LoginResponse(
                message = "",
                usuario = user
            )
            
            response.message.shouldBe("")
        }
        
        it("Debería detectar LoginResponse iguales") {
            val user = User(id = 1, nombre = "John", email = "john@test.com")
            val response1 = LoginResponse(message = "Éxito", usuario = user)
            val response2 = LoginResponse(message = "Éxito", usuario = user)
            
            response1.shouldBe(response2)
        }
        
        it("Debería acceder a propiedades anidadas correctamente") {
            val user = User(id = 1, nombre = "John", email = "john@test.com", password = "Pass123")
            val response = LoginResponse(message = "Login exitoso", usuario = user)
            
            response.usuario.id.shouldBe(1)
            response.usuario.nombre.shouldBe("John")
            response.usuario.email.shouldBe("john@test.com")
            response.usuario.password.shouldBe("Pass123")
        }
    }
})
