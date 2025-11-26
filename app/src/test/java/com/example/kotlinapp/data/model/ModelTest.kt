package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GameTest : DescribeSpec({
    
    describe("Game - Propiedades y compatibilidad") {
        
        it("Debería crear un juego con valores por defecto") {
            val game = Game()
            
            game.id.shouldBe(0)
            game.title.shouldBe("")
            game.price.shouldBe(0.0)
            game.rating.shouldBe(0f)
            game.featured.shouldBe(0)
        }
        
        it("Debería crear un juego con valores específicos") {
            val game = Game(
                id = 1,
                title = "Elden Ring",
                price = 59.99,
                rating = 4.8f,
                image = "https://example.com/elden-ring.jpg"
            )
            
            game.id.shouldBe(1)
            game.title.shouldBe("Elden Ring")
            game.price.shouldBe(59.99)
            game.rating.shouldBe(4.8f)
        }
        
        it("Debería acceder a la propiedad 'name' como alias de 'title'") {
            val game = Game(title = "Cyberpunk 2077")
            
            game.name.shouldBe("Cyberpunk 2077")
        }
        
        it("Debería acceder a 'imageUrl' como alias de 'image'") {
            val game = Game(image = "https://example.com/image.jpg")
            
            game.imageUrl.shouldBe("https://example.com/image.jpg")
        }
        
        it("Debería convertir 'featured' a booleano correctamente") {
            val gameFeatured = Game(featured = 1)
            val gameNotFeatured = Game(featured = 0)
            
            gameFeatured.isFeatured.shouldBe(true)
            gameNotFeatured.isFeatured.shouldBe(false)
        }
        
        it("Debería manejar descuentos correctamente") {
            val game = Game(
                price = 59.99,
                originalPrice = 79.99,
                discount = 25.0
            )
            
            game.price.shouldBe(59.99)
            game.originalPrice.shouldBe(79.99)
            game.discount.shouldBe(25.0)
        }
        
        it("Debería manejar múltiples plataformas") {
            val game = Game(
                platform = listOf("PC", "PlayStation 5", "Xbox Series X")
            )
            
            game.platform.shouldBe(listOf("PC", "PlayStation 5", "Xbox Series X"))
            game.platform.size.shouldBe(3)
        }
        
        it("Debería manejar características del juego") {
            val game = Game(
                features = listOf("Multijugador", "Modo Historia", "PvP")
            )
            
            game.features.shouldBe(listOf("Multijugador", "Modo Historia", "PvP"))
        }
    }
})

class GameRequirementsTest : DescribeSpec({
    
    describe("GameRequirements - Requisitos del sistema") {
        
        it("Debería crear requisitos del sistema con valores por defecto") {
            val requirements = GameRequirements()
            
            requirements.os.shouldBe("")
            requirements.processor.shouldBe("")
            requirements.memory.shouldBe("")
            requirements.graphics.shouldBe("")
            requirements.storage.shouldBe("")
        }
        
        it("Debería crear requisitos del sistema con valores específicos") {
            val requirements = GameRequirements(
                os = "Windows 10 64-bit",
                processor = "Intel Core i7-9700K",
                memory = "16 GB RAM",
                graphics = "NVIDIA GeForce RTX 3080",
                storage = "150 GB SSD"
            )
            
            requirements.os.shouldBe("Windows 10 64-bit")
            requirements.processor.shouldBe("Intel Core i7-9700K")
            requirements.memory.shouldBe("16 GB RAM")
            requirements.graphics.shouldBe("NVIDIA GeForce RTX 3080")
            requirements.storage.shouldBe("150 GB SSD")
        }
    }
})

class UserTest : DescribeSpec({
    
    describe("User - Datos de usuario") {
        
        it("Debería crear un usuario con valores por defecto") {
            val user = User()
            
            user.id.shouldBe(0)
            user.nombre.shouldBe("")
            user.email.shouldBe("")
            user.password.shouldBe("")
            user.numero.shouldBe(null)
        }
        
        it("Debería crear un usuario con valores específicos") {
            val user = User(
                id = 1,
                nombre = "John Doe",
                email = "john@example.com",
                password = "Password123!",
                numero = "555-1234"
            )
            
            user.id.shouldBe(1)
            user.nombre.shouldBe("John Doe")
            user.email.shouldBe("john@example.com")
            user.numero.shouldBe("555-1234")
        }
        
        it("Debería permitir número de teléfono opcional") {
            val userWithoutNumber = User(nombre = "Jane", email = "jane@example.com")
            val userWithNumber = User(nombre = "Jane", email = "jane@example.com", numero = "555-5678")
            
            userWithoutNumber.numero.shouldBe(null)
            userWithNumber.numero.shouldBe("555-5678")
        }
    }
})

class CartItemTest : DescribeSpec({
    
    describe("CartItem - Producto en carrito") {
        
        it("Debería crear un item de carrito con cantidad por defecto") {
            val game = Game(
                id = 1,
                title = "Elden Ring",
                price = 59.99
            )
            val cartItem = CartItem(game)
            
            cartItem.game.shouldBe(game)
            cartItem.quantity.shouldBe(1)
        }
        
        it("Debería crear un item de carrito con cantidad específica") {
            val game = Game(
                id = 1,
                title = "Elden Ring",
                price = 59.99
            )
            val cartItem = CartItem(game, quantity = 3)
            
            cartItem.game.shouldBe(game)
            cartItem.quantity.shouldBe(3)
        }
        
        it("Debería calcular el precio total del item") {
            val game = Game(
                id = 1,
                title = "Elden Ring",
                price = 59.99
            )
            val cartItem = CartItem(game, quantity = 2)
            
            val totalPrice = cartItem.game.price * cartItem.quantity
            totalPrice.shouldBe(119.98)
        }
    }
})

class LoginRequestTest : DescribeSpec({
    
    describe("LoginRequest - Solicitud de login") {
        
        it("Debería crear una solicitud de login") {
            val request = LoginRequest(
                email = "user@example.com",
                password = "Password123!"
            )
            
            request.email.shouldBe("user@example.com")
            request.password.shouldBe("Password123!")
        }
    }
})

class LoginResponseTest : DescribeSpec({
    
    describe("LoginResponse - Respuesta de login") {
        
        it("Debería crear una respuesta de login exitosa") {
            val user = User(
                id = 1,
                nombre = "John Doe",
                email = "john@example.com"
            )
            val response = LoginResponse(
                message = "Login exitoso",
                usuario = user
            )
            
            response.message.shouldBe("Login exitoso")
            response.usuario.id.shouldBe(1)
            response.usuario.nombre.shouldBe("John Doe")
        }
    }
})

class RegisterRequestTest : DescribeSpec({
    
    describe("RegisterRequest - Solicitud de registro") {
        
        it("Debería crear una solicitud de registro sin número") {
            val request = RegisterRequest(
                nombre = "Jane Doe",
                email = "jane@example.com",
                password = "SecurePass123!"
            )
            
            request.nombre.shouldBe("Jane Doe")
            request.email.shouldBe("jane@example.com")
            request.password.shouldBe("SecurePass123!")
            request.numero.shouldBe(null)
        }
        
        it("Debería crear una solicitud de registro con número") {
            val request = RegisterRequest(
                nombre = "Jane Doe",
                email = "jane@example.com",
                password = "SecurePass123!",
                numero = "555-9999"
            )
            
            request.numero.shouldBe("555-9999")
        }
    }
})
