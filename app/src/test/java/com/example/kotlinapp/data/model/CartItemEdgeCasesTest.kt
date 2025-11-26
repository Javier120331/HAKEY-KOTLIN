package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CartItemEdgeCasesTest : DescribeSpec({
    
    describe("CartItem - Casos límite") {
        
        it("Debería manejar cantidad 0") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem = CartItem(game, quantity = 0)
            
            cartItem.quantity.shouldBe(0)
        }
        
        it("Debería manejar cantidad 1 (default)") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem = CartItem(game)
            
            cartItem.quantity.shouldBe(1)
        }
        
        it("Debería manejar cantidad muy grande") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem = CartItem(game, quantity = 1000000)
            
            cartItem.quantity.shouldBe(1000000)
        }
        
        it("Debería calcular precio total correctamente") {
            val game = Game(id = 1, title = "Test", price = 59.99)
            val cartItem = CartItem(game, quantity = 3)
            
            val totalPrice = cartItem.game.price * cartItem.quantity
            totalPrice.shouldBe(179.97)
        }
        
        it("Debería calcular precio total con cantidad 0") {
            val game = Game(id = 1, title = "Test", price = 59.99)
            val cartItem = CartItem(game, quantity = 0)
            
            val totalPrice = cartItem.game.price * cartItem.quantity
            totalPrice.shouldBe(0.0)
        }
        
        it("Debería calcular precio total con juego gratis") {
            val game = Game(id = 1, title = "Free Game", price = 0.0)
            val cartItem = CartItem(game, quantity = 5)
            
            val totalPrice = cartItem.game.price * cartItem.quantity
            totalPrice.shouldBe(0.0)
        }
        
        it("Debería manejar juego con precio muy alto") {
            val game = Game(id = 1, title = "Expensive", price = 9999.99)
            val cartItem = CartItem(game, quantity = 2)
            
            val totalPrice = cartItem.game.price * cartItem.quantity
            totalPrice.shouldBe(19999.98)
        }
        
        it("Debería permitir acceder a propiedades del juego") {
            val game = Game(id = 1, title = "Test", price = 50.0, rating = 4.5f)
            val cartItem = CartItem(game, quantity = 2)
            
            cartItem.game.id.shouldBe(1)
            cartItem.game.title.shouldBe("Test")
            cartItem.game.price.shouldBe(50.0)
            cartItem.game.rating.shouldBe(4.5f)
        }
        
        it("Debería mantener referencia al mismo juego") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem = CartItem(game, quantity = 3)
            
            cartItem.game.shouldBe(game)
        }
        
        it("Debería ser un data class con properties") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem1 = CartItem(game, quantity = 1)
            val cartItem2 = CartItem(game, quantity = 1)
            
            // Data class equality
            cartItem1.shouldBe(cartItem2)
        }
    }
    
    describe("CartItem - Operaciones de copia") {
        
        it("Debería permitir copiar CartItem con cambio de cantidad") {
            val game = Game(id = 1, title = "Test", price = 50.0)
            val cartItem1 = CartItem(game, quantity = 1)
            val cartItem2 = cartItem1.copy(quantity = 5)
            
            cartItem1.quantity.shouldBe(1)
            cartItem2.quantity.shouldBe(5)
            cartItem2.game.shouldBe(game)
        }
        
        it("Debería permitir copiar CartItem con cambio de juego") {
            val game1 = Game(id = 1, title = "Game1", price = 50.0)
            val game2 = Game(id = 2, title = "Game2", price = 60.0)
            
            val cartItem1 = CartItem(game1, quantity = 2)
            val cartItem2 = cartItem1.copy(game = game2)
            
            cartItem1.game.id.shouldBe(1)
            cartItem2.game.id.shouldBe(2)
            cartItem2.quantity.shouldBe(2)
        }
        
        it("Debería permitir copiar CartItem con múltiples cambios") {
            val game1 = Game(id = 1, title = "Game1", price = 50.0)
            val game2 = Game(id = 2, title = "Game2", price = 60.0)
            
            val cartItem1 = CartItem(game1, quantity = 1)
            val cartItem2 = cartItem1.copy(game = game2, quantity = 10)
            
            cartItem1.game.id.shouldBe(1)
            cartItem1.quantity.shouldBe(1)
            cartItem2.game.id.shouldBe(2)
            cartItem2.quantity.shouldBe(10)
        }
    }
})
