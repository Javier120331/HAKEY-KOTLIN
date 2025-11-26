package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize

class ModelDataValidationTest : DescribeSpec({
    
    describe("Game - Validaciones de negocio") {
        
        it("Debería copiar juego manteniendo todos los campos") {
            val original = Game(
                id = 1,
                title = "Original",
                price = 50.0,
                rating = 4.5f,
                featured = 1,
                image = "https://example.com/img.jpg",
                description = "Description",
                category = "RPG",
                platform = listOf("PC"),
                releaseDate = "2024-01-01",
                publisher = "Publisher"
            )
            
            val copied = original.copy()
            
            copied.shouldBe(original)
            copied.id.shouldBe(1)
            copied.title.shouldBe("Original")
        }
        
        it("Debería modificar solo algunos campos en copy") {
            val original = Game(
                id = 1,
                title = "Original",
                price = 50.0,
                rating = 4.5f
            )
            
            val modified = original.copy(title = "Modified", price = 40.0)
            
            modified.id.shouldBe(1)
            modified.title.shouldBe("Modified")
            modified.price.shouldBe(40.0)
            modified.rating.shouldBe(4.5f)
        }
        
        it("Debería tener alias de propiedad name") {
            val game = Game(title = "Test Game")
            
            game.name.shouldBe(game.title)
            game.name.shouldBe("Test Game")
        }
        
        it("Debería tener alias de propiedad imageUrl") {
            val game = Game(image = "https://example.com/img.jpg")
            
            game.imageUrl.shouldBe(game.image)
        }
        
        it("Debería convertir featured a booleano correctamente") {
            val gameFeatured = Game(featured = 1)
            val gameNotFeatured = Game(featured = 0)
            val gameOtherValue = Game(featured = 5)
            
            gameFeatured.isFeatured.shouldBe(true)
            gameNotFeatured.isFeatured.shouldBe(false)
            gameOtherValue.isFeatured.shouldBe(false)
        }
    }
    
    describe("CartItem - Gestión de carrito") {
        
        it("Debería crear CartItem con cantidad por defecto") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem = CartItem(game)
            
            cartItem.game.shouldBe(game)
            cartItem.quantity.shouldBe(1)
        }
        
        it("Debería crear CartItem con cantidad específica") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem = CartItem(game, quantity = 5)
            
            cartItem.quantity.shouldBe(5)
        }
        
        it("Debería permitir copiar CartItem con cambios") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val original = CartItem(game, quantity = 3)
            val modified = original.copy(quantity = 5)
            
            original.quantity.shouldBe(3)
            modified.quantity.shouldBe(5)
            modified.game.shouldBe(game)
        }
    }
    
    describe("User - Datos de usuario") {
        
        it("Debería copiar usuario con nuevos valores") {
            val original = User(
                id = 1,
                nombre = "Juan",
                email = "juan@example.com",
                password = "pass123",
                numero = "123456"
            )
            
            val modified = original.copy(nombre = "Carlos")
            
            modified.id.shouldBe(1)
            modified.nombre.shouldBe("Carlos")
            modified.email.shouldBe("juan@example.com")
        }
        
        it("Debería manejar usuario sin teléfono") {
            val user = User(
                id = 1,
                nombre = "Juan",
                email = "juan@example.com"
            )
            
            user.numero.shouldBe(null)
        }
        
        it("Debería manejar usuario con todos los datos") {
            val user = User(
                id = 1,
                nombre = "Juan Pérez",
                email = "juan@example.com",
                password = "SecurePass123!",
                numero = "+56912345678"
            )
            
            user.nombre.shouldBe("Juan Pérez")
            user.email.shouldBe("juan@example.com")
            user.numero.shouldBe("+56912345678")
        }
    }
    
    describe("LoginRequest - Solicitud de autenticación") {
        
        it("Debería crear LoginRequest con credenciales") {
            val request = LoginRequest(
                email = "test@example.com",
                password = "password123"
            )
            
            request.email.shouldBe("test@example.com")
            request.password.shouldBe("password123")
        }
    }
    
    describe("LoginResponse - Respuesta de autenticación") {
        
        it("Debería crear LoginResponse con usuario") {
            val user = User(id = 1, nombre = "Test", email = "test@example.com")
            val response = LoginResponse(
                message = "Login successful",
                usuario = user
            )
            
            response.message.shouldBe("Login successful")
            response.usuario.shouldBe(user)
            response.usuario.id.shouldBe(1)
        }
    }
    
    describe("RegisterRequest - Solicitud de registro") {
        
        it("Debería crear RegisterRequest sin teléfono") {
            val request = RegisterRequest(
                nombre = "Juan",
                email = "juan@example.com",
                password = "pass123"
            )
            
            request.nombre.shouldBe("Juan")
            request.email.shouldBe("juan@example.com")
            request.numero.shouldBe(null)
        }
        
        it("Debería crear RegisterRequest con teléfono") {
            val request = RegisterRequest(
                nombre = "Juan",
                email = "juan@example.com",
                password = "pass123",
                numero = "123456"
            )
            
            request.numero.shouldBe("123456")
        }
    }
    
    describe("GameRequirements - Requisitos de sistema") {
        
        it("Debería crear GameRequirements vacío") {
            val req = GameRequirements()
            
            req.os.shouldBe("")
            req.processor.shouldBe("")
            req.memory.shouldBe("")
            req.graphics.shouldBe("")
            req.storage.shouldBe("")
        }
        
        it("Debería crear GameRequirements con valores") {
            val req = GameRequirements(
                os = "Windows 10",
                processor = "Intel i7",
                memory = "16GB",
                graphics = "RTX 3080",
                storage = "150GB"
            )
            
            req.os.shouldBe("Windows 10")
            req.processor.shouldBe("Intel i7")
            req.memory.shouldBe("16GB")
            req.graphics.shouldBe("RTX 3080")
            req.storage.shouldBe("150GB")
        }
        
        it("Debería copiar GameRequirements con cambios") {
            val original = GameRequirements(
                os = "Windows 10",
                memory = "16GB"
            )
            
            val modified = original.copy(memory = "32GB")
            
            modified.os.shouldBe("Windows 10")
            modified.memory.shouldBe("32GB")
        }
    }
    
    describe("Game - Listas de características") {
        
        it("Debería manejar lista vacía de plataformas") {
            val game = Game(
                id = 1,
                title = "Test",
                platform = emptyList()
            )
            
            game.platform.shouldHaveSize(0)
        }
        
        it("Debería manejar múltiples plataformas") {
            val platforms = listOf("PC", "PlayStation 5", "Xbox Series X", "Nintendo Switch")
            val game = Game(
                id = 1,
                title = "Test",
                platform = platforms
            )
            
            game.platform.shouldHaveSize(4)
            game.platform[0].shouldBe("PC")
            game.platform[3].shouldBe("Nintendo Switch")
        }
        
        it("Debería manejar lista vacía de features") {
            val game = Game(
                id = 1,
                title = "Test",
                features = emptyList()
            )
            
            game.features.shouldHaveSize(0)
        }
        
        it("Debería manejar múltiples features") {
            val features = listOf("Multijugador", "Modo Historia", "PvP", "Cross-Platform")
            val game = Game(
                id = 1,
                title = "Test",
                features = features
            )
            
            game.features.shouldHaveSize(4)
            game.features.shouldHaveSize(features.size)
        }
    }
})
