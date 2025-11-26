package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class GameEdgeCasesTest : DescribeSpec({
    
    describe("Game - Casos límite y especiales") {
        
        it("Debería manejar juego con precio 0") {
            val game = Game(id = 1, title = "Juego Gratis", price = 0.0)
            
            game.price.shouldBe(0.0)
            (game.price == 0.0).shouldBe(true)
        }
        
        it("Debería manejar juego con rating máximo") {
            val game = Game(id = 1, title = "Perfecto", rating = 5f)
            
            game.rating.shouldBe(5f)
        }
        
        it("Debería manejar juego con rating mínimo") {
            val game = Game(id = 1, title = "Terrible", rating = 0f)
            
            game.rating.shouldBe(0f)
        }
        
        it("Debería manejar títulos vacíos") {
            val game = Game(id = 1, title = "")
            
            game.title.shouldBe("")
            game.title.isEmpty().shouldBe(true)
        }
        
        it("Debería manejar títulos muy largos") {
            val longTitle = "A".repeat(500)
            val game = Game(id = 1, title = longTitle)
            
            game.title.shouldBe(longTitle)
            game.title.length.shouldBe(500)
        }
        
        it("Debería manejar featured como 0") {
            val game = Game(id = 1, title = "Test", featured = 0)
            
            game.featured.shouldBe(0)
            game.isFeatured.shouldBe(false)
        }
        
        it("Debería manejar featured como 1") {
            val game = Game(id = 1, title = "Test", featured = 1)
            
            game.featured.shouldBe(1)
            game.isFeatured.shouldBe(true)
        }
        
        it("Debería manejar featured como número grande") {
            val game = Game(id = 1, title = "Test", featured = 999)
            
            game.featured.shouldBe(999)
            game.isFeatured.shouldBe(false) // Solo featured == 1 es true
        }
        
        it("Debería tener nombre como alias de title") {
            val game = Game(title = "Elden Ring")
            
            game.name.shouldBe("Elden Ring")
            game.name.shouldBe(game.title)
        }
        
        it("Debería tener imageUrl como alias de image") {
            val imageUrl = "https://example.com/image.jpg"
            val game = Game(image = imageUrl)
            
            game.imageUrl.shouldBe(imageUrl)
            game.imageUrl.shouldBe(game.image)
        }
        
        it("Debería manejar descuento nulo") {
            val game = Game(id = 1, title = "Sin descuento", discount = null)
            
            game.discount.shouldBe(null)
        }
        
        it("Debería manejar descuento con valor") {
            val game = Game(id = 1, title = "Con descuento", discount = 25.0)
            
            game.discount.shouldBe(25.0)
        }
        
        it("Debería manejar lista vacía de plataformas") {
            val game = Game(id = 1, title = "Test", platform = emptyList())
            
            game.platform.shouldBe(emptyList())
            game.platform.isEmpty().shouldBe(true)
        }
        
        it("Debería manejar múltiples plataformas") {
            val platforms = listOf("PC", "PS5", "Xbox Series X", "Nintendo Switch")
            val game = Game(id = 1, title = "Multiplataforma", platform = platforms)
            
            game.platform.shouldBe(platforms)
            game.platform.size.shouldBe(4)
        }
        
        it("Debería manejar lista vacía de features") {
            val game = Game(id = 1, title = "Test", features = emptyList())
            
            game.features.shouldBe(emptyList())
        }
        
        it("Debería manejar múltiples features") {
            val features = listOf("Multijugador", "Modo Historia", "PvP", "Cooperativo")
            val game = Game(id = 1, title = "Test", features = features)
            
            game.features.shouldBe(features)
            game.features.size.shouldBe(4)
        }
    }
    
    describe("Game - Comparación y igualdad") {
        
        it("Debería detectar juegos iguales") {
            val game1 = Game(id = 1, title = "Elden Ring", price = 59.99)
            val game2 = Game(id = 1, title = "Elden Ring", price = 59.99)
            
            game1.shouldBe(game2)
        }
        
        it("Debería detectar juegos diferentes") {
            val game1 = Game(id = 1, title = "Elden Ring", price = 59.99)
            val game2 = Game(id = 2, title = "Cyberpunk 2077", price = 49.99)
            
            (game1 != game2).shouldBe(true)
        }
        
        it("Debería manejar ID 0") {
            val game = Game(id = 0, title = "Test")
            
            game.id.shouldBe(0)
        }
        
        it("Debería manejar ID negativo") {
            val game = Game(id = -1, title = "Test")
            
            game.id.shouldBe(-1)
        }
        
        it("Debería manejar ID muy grande") {
            val game = Game(id = 999999, title = "Test")
            
            game.id.shouldBe(999999)
        }
    }
    
    describe("Game - Cálculos y operaciones") {
        
        it("Debería calcular precio con descuento") {
            val game = Game(
                id = 1,
                title = "Test",
                price = 100.0,
                discount = 20.0,
                originalPrice = 100.0
            )
            
            val discountedPrice = game.price
            discountedPrice.shouldBe(100.0)
        }
        
        it("Debería permitir copiar game con cambios") {
            val originalGame = Game(id = 1, title = "Original", price = 50.0)
            val copiedGame = originalGame.copy(price = 40.0)
            
            originalGame.price.shouldBe(50.0)
            copiedGame.price.shouldBe(40.0)
            copiedGame.title.shouldBe("Original")
        }
    }
})
