package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.model.CartItem
import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize

class ShoppingCartRepositoryTest : DescribeSpec({
    
    val repository = ShoppingCartRepository()
    
    val testGame1 = Game(
        id = 1,
        title = "Elden Ring",
        price = 59.99,
        rating = 4.8f,
        image = "https://example.com/elden-ring.jpg"
    )
    
    val testGame2 = Game(
        id = 2,
        title = "Cyberpunk 2077",
        price = 49.99,
        rating = 4.5f,
        image = "https://example.com/cyberpunk.jpg"
    )
    
    describe("ShoppingCartRepository - Agregar productos") {
        
        it("Debería agregar un juego al carrito") {
            repository.clearCart()
            repository.addToCart(testGame1)
            
            repository.getCartItems().shouldHaveSize(1)
            repository.getCartItems()[0].game.title.shouldBe("Elden Ring")
            repository.getCartItems()[0].quantity.shouldBe(1)
        }
        
        it("Debería aumentar la cantidad si el juego ya existe en el carrito") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame1)
            
            repository.getCartItems().shouldHaveSize(1)
            repository.getCartItems()[0].quantity.shouldBe(2)
        }
        
        it("Debería agregar múltiples juegos diferentes al carrito") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame2)
            
            repository.getCartItems().shouldHaveSize(2)
        }
    }
    
    describe("ShoppingCartRepository - Remover productos") {
        
        it("Debería remover un juego del carrito") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame2)
            
            repository.removeFromCart(1)
            
            repository.getCartItems().shouldHaveSize(1)
            repository.getCartItems()[0].game.id.shouldBe(2)
        }
        
        it("Debería disminuir la cantidad del juego") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame1)
            repository.decreaseQuantity(1)
            
            repository.getCartItems()[0].quantity.shouldBe(1)
        }
        
        it("Debería remover el juego cuando la cantidad llega a cero") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.decreaseQuantity(1)
            
            repository.getCartItems().shouldHaveSize(0)
        }
    }
    
    describe("ShoppingCartRepository - Cálculos totales") {
        
        it("Debería calcular el precio total correctamente") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame2)
            
            val expectedTotal = 59.99 + 49.99
            repository.getTotalPrice().shouldBe(expectedTotal)
        }
        
        it("Debería calcular el precio total con múltiples cantidades") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame1)
            repository.addToCart(testGame2)
            
            val expectedTotal = (59.99 * 2) + 49.99
            repository.getTotalPrice().shouldBe(expectedTotal)
        }
        
        it("Debería retornar la cantidad de items en el carrito") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame2)
            
            repository.getCartCount().shouldBe(2)
        }
    }
    
    describe("ShoppingCartRepository - Manejo del carrito") {
        
        it("Debería limpiar el carrito") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame2)
            
            repository.clearCart()
            
            repository.getCartItems().shouldHaveSize(0)
            repository.getTotalPrice().shouldBe(0.0)
        }
        
        it("Debería registrar mensaje de acción al agregar producto") {
            repository.clearCart()
            repository.addToCart(testGame1)
            
            repository.lastActionMessage.shouldBe("Agregado: ${testGame1.name}")
        }
        
        it("Debería actualizar mensaje cuando se aumenta la cantidad") {
            repository.clearCart()
            repository.addToCart(testGame1)
            repository.addToCart(testGame1)
            
            repository.lastActionMessage.shouldBe("Agregado: ${testGame1.name} (x2)")
        }
    }
})
