package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CartItemAdvancedTest : DescribeSpec({
    
    val testGame = Game(
        id = 1,
        title = "Test Game",
        price = 49.99,
        rating = 4.5f,
        image = "https://example.com/game.jpg"
    )
    
    describe("CartItem - Propiedades") {
        
        it("Debería permitir crear CartItem sin especificar cantidad") {
            val item = CartItem(testGame)
            
            item.game.shouldBe(testGame)
            item.quantity.shouldBe(1)
        }
        
        it("Debería permitir crear CartItem con cantidad específica") {
            val item = CartItem(testGame, quantity = 5)
            
            item.game.shouldBe(testGame)
            item.quantity.shouldBe(5)
        }
    }
    
    describe("CartItem - Cálculos") {
        
        it("Debería calcular precio total correctamente para cantidad 1") {
            val item = CartItem(testGame, quantity = 1)
            val total = item.game.price * item.quantity
            
            total.shouldBe(49.99)
        }
        
        it("Debería calcular precio total correctamente para cantidad múltiple") {
            val item = CartItem(testGame, quantity = 3)
            val total = item.game.price * item.quantity
            
            total.shouldBe(149.97)
        }
        
        it("Debería manejar cantidad cero") {
            val item = CartItem(testGame, quantity = 0)
            val total = item.game.price * item.quantity
            
            total.shouldBe(0.0)
        }
    }
    
    describe("CartItem - Igualdad") {
        
        it("Debería dos CartItems con mismo game e id ser comparables") {
            val item1 = CartItem(testGame, quantity = 2)
            val item2 = CartItem(testGame, quantity = 2)
            
            item1.game.id.shouldBe(item2.game.id)
        }
        
        it("Debería permitir actualizar cantidad del item") {
            val item = CartItem(testGame, quantity = 1)
            val updatedItem = item.copy(quantity = 5)
            
            item.quantity.shouldBe(1)
            updatedItem.quantity.shouldBe(5)
            updatedItem.game.shouldBe(testGame)
        }
    }
})

class GameAdvancedPropertiesTest : DescribeSpec({
    
    val testGame = Game(
        id = 1,
        title = "Elden Ring",
        price = 59.99,
        rating = 4.8f,
        featured = 1,
        image = "https://example.com/elden.jpg",
        category = "RPG",
        description = "Un juego de acción RPG",
        releaseDate = "2022-02-25",
        publisher = "FromSoftware"
    )
    
    describe("Game - Aliases de propiedades") {
        
        it("Debería acceder a title mediante 'name'") {
            testGame.name.shouldBe("Elden Ring")
            testGame.name.shouldBe(testGame.title)
        }
        
        it("Debería acceder a image mediante 'imageUrl'") {
            testGame.imageUrl.shouldBe("https://example.com/elden.jpg")
            testGame.imageUrl.shouldBe(testGame.image)
        }
    }
    
    describe("Game - Conversión de featured") {
        
        it("Debería convertir featured 1 a true") {
            val featuredGame = testGame.copy(featured = 1)
            featuredGame.isFeatured.shouldBe(true)
        }
        
        it("Debería convertir featured 0 a false") {
            val notFeaturedGame = testGame.copy(featured = 0)
            notFeaturedGame.isFeatured.shouldBe(false)
        }
    }
    
    describe("Game - Datos opcionales") {
        
        it("Debería permitir crear Game sin precio original") {
            val game = Game(title = "Test", price = 29.99)
            game.originalPrice.shouldBe(null)
        }
        
        it("Debería permitir crear Game con precio original") {
            val game = Game(
                title = "Test",
                price = 29.99,
                originalPrice = 49.99
            )
            game.originalPrice.shouldBe(49.99)
        }
        
        it("Debería permitir crear Game con descuento") {
            val game = Game(
                title = "Test",
                price = 29.99,
                discount = 40.0
            )
            game.discount.shouldBe(40.0)
        }
    }
    
    describe("Game - Listas de propiedades") {
        
        it("Debería permitir agregar plataformas") {
            val game = Game(
                title = "Test",
                price = 49.99,
                platform = listOf("PC", "PlayStation 5")
            )
            
            game.platform.shouldBe(listOf("PC", "PlayStation 5"))
        }
        
        it("Debería permitir agregar features") {
            val game = Game(
                title = "Test",
                price = 49.99,
                features = listOf("Modo Historia", "Multijugador")
            )
            
            game.features.shouldBe(listOf("Modo Historia", "Multijugador"))
        }
    }
})
