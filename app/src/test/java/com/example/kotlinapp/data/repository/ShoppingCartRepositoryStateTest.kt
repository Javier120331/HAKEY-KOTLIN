package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize

class ShoppingCartRepositoryStateTest : DescribeSpec({
    
    val game1 = Game(id = 1, title = "Game 1", price = 30.0)
    val game2 = Game(id = 2, title = "Game 2", price = 40.0)
    val game3 = Game(id = 3, title = "Game 3", price = 50.0)
    
    describe("ShoppingCartRepository - Flujo completo de compra") {
        
        it("Debería iniciar carrito vacío") {
            val cart = ShoppingCartRepository()
            
            cart.getCartCount().shouldBe(0)
            cart.getTotalPrice().shouldBe(0.0)
            cart.getCartItems().shouldBeEmpty()
        }
        
        it("Debería agregar producto y verificar estado") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            
            cart.getCartCount().shouldBe(1)
            cart.getTotalPrice().shouldBe(30.0)
            cart.lastActionMessage?.contains("Game 1").shouldBe(true)
        }
        
        it("Debería agregar múltiples productos diferentes") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            cart.addToCart(game3)
            
            cart.getCartCount().shouldBe(3)
            val expectedTotal = 30.0 + 40.0 + 50.0
            cart.getTotalPrice().shouldBe(expectedTotal)
        }
        
        it("Debería incrementar cantidad del mismo producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            val firstCount = cart.getCartCount()
            
            cart.addToCart(game1)
            val secondCount = cart.getCartCount()
            
            firstCount.shouldBe(1)
            secondCount.shouldBe(1)
            cart.getCartItems().first().quantity.shouldBe(2)
        }
        
        it("Debería calcular precio total correcto con cantidades") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1) // 30.0
            cart.addToCart(game1) // 30.0 x2 = 60.0
            cart.addToCart(game2) // 40.0
            
            val expectedTotal = 60.0 + 40.0
            cart.getTotalPrice().shouldBe(expectedTotal)
        }
    }
    
    describe("ShoppingCartRepository - Decrementar cantidad") {
        
        it("Debería disminuir cantidad de producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game1)
            cart.addToCart(game1)
            
            cart.decreaseQuantity(game1.id)
            
            cart.getCartCount().shouldBe(1)
            cart.getCartItems().first().quantity.shouldBe(2)
            cart.getTotalPrice().shouldBe(60.0)
        }
        
        it("Debería remover producto cuando cantidad es 1") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            
            cart.decreaseQuantity(game1.id)
            
            cart.getCartCount().shouldBe(1)
            cart.getCartItems().first().game.id.shouldBe(2)
            cart.getTotalPrice().shouldBe(40.0)
        }
        
        it("Debería mantener otros productos al decrementar") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game1)
            cart.addToCart(game2)
            cart.addToCart(game3)
            
            cart.decreaseQuantity(game1.id)
            
            cart.getCartCount().shouldBe(3)
            val items = cart.getCartItems()
            items[0].game.id.shouldBe(1)
            items[0].quantity.shouldBe(1)
        }
    }
    
    describe("ShoppingCartRepository - Remover productos") {
        
        it("Debería remover un producto específico") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            cart.addToCart(game3)
            
            cart.removeFromCart(game2.id)
            
            cart.getCartCount().shouldBe(2)
            val items = cart.getCartItems()
            items.none { it.game.id == 2 }.shouldBe(true)
        }
        
        it("Debería remover producto con múltiples cantidades") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game1)
            cart.addToCart(game1)
            
            cart.removeFromCart(game1.id)
            
            cart.getCartCount().shouldBe(0)
            cart.getTotalPrice().shouldBe(0.0)
        }
        
        it("No debería fallar al remover producto inexistente") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.removeFromCart(999)
            
            cart.getCartCount().shouldBe(1)
        }
    }
    
    describe("ShoppingCartRepository - Limpiar y vaciar") {
        
        it("Debería vaciar carrito completamente") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            cart.addToCart(game3)
            
            cart.clearCart()
            
            cart.getCartCount().shouldBe(0)
            cart.getTotalPrice().shouldBe(0.0)
            cart.getCartItems().shouldBeEmpty()
        }
    }
    
    describe("ShoppingCartRepository - Acceso a items") {
        
        it("Debería retornar lista inmutable de items") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            
            val items = cart.getCartItems()
            
            items.shouldHaveSize(2)
            items[0].game.title.shouldBe("Game 1")
            items[1].game.title.shouldBe("Game 2")
        }
        
        it("Debería mantener orden de inserción") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game3)
            cart.addToCart(game1)
            cart.addToCart(game2)
            
            val items = cart.getCartItems()
            
            items[0].game.id.shouldBe(3)
            items[1].game.id.shouldBe(1)
            items[2].game.id.shouldBe(2)
        }
    }
    
    describe("ShoppingCartRepository - Cálculos de precio") {
        
        it("Debería calcular correctamente con un producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            
            cart.getTotalPrice().shouldBe(30.0)
        }
        
        it("Debería calcular correctamente con múltiples productos") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            cart.addToCart(game3)
            
            cart.getTotalPrice().shouldBe(120.0)
        }
        
        it("Debería actualizar total después de decrementar") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game1)
            
            val beforeDecrease = cart.getTotalPrice()
            cart.decreaseQuantity(game1.id)
            val afterDecrease = cart.getTotalPrice()
            
            beforeDecrease.shouldBe(60.0)
            afterDecrease.shouldBe(30.0)
        }
        
        it("Debería actualizar total después de remover") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            
            val beforeRemove = cart.getTotalPrice()
            cart.removeFromCart(game1.id)
            val afterRemove = cart.getTotalPrice()
            
            beforeRemove.shouldBe(70.0)
            afterRemove.shouldBe(40.0)
        }
    }
    
    describe("ShoppingCartRepository - Mensajes de acción") {
        
        it("Debería generar mensaje al agregar") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            
            cart.lastActionMessage?.contains("Game 1").shouldBe(true)
            cart.lastActionMessage?.contains("Agregado").shouldBe(true)
        }
        
        it("Debería actualizar mensaje al agregar múltiples") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            val msg1 = cart.lastActionMessage
            
            cart.addToCart(game2)
            val msg2 = cart.lastActionMessage
            
            msg1?.contains("Game 1").shouldBe(true)
            msg2?.contains("Game 2").shouldBe(true)
            msg1.shouldBe(msg1) // Primera no cambia
            msg2.shouldBe(msg2) // Segunda es diferente
        }
        
        it("Debería mostrar cantidad en mensaje cuando se incrementa") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(game1)
            cart.addToCart(game1)
            
            cart.lastActionMessage?.contains("x2").shouldBe(true)
        }
    }
})
