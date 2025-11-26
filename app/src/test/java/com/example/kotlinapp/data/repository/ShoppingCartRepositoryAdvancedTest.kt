package com.example.kotlinapp.data.repository

import com.example.kotlinapp.data.model.CartItem
import com.example.kotlinapp.data.model.Game
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain

class ShoppingCartRepositoryAdvancedTest : DescribeSpec({
    
    val testGames = listOf(
        Game(id = 1, title = "Elden Ring", price = 59.99),
        Game(id = 2, title = "Cyberpunk 2077", price = 49.99),
        Game(id = 3, title = "The Witcher 3", price = 39.99),
        Game(id = 4, title = "Dark Souls 3", price = 29.99)
    )
    
    describe("ShoppingCartRepository - Operaciones complejas") {
        
        it("Debería agregar múltiples productos diferentes") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[1])
            cart.addToCart(testGames[2])
            
            cart.getCartCount().shouldBe(3)
        }
        
        it("Debería incrementar cantidad del mismo producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[0])
            
            cart.getCartCount().shouldBe(1)
            cart.getCartItems().first().quantity.shouldBe(3)
        }
        
        it("Debería calcular total correcto con múltiples items") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0]) // 59.99
            cart.addToCart(testGames[1]) // 49.99
            cart.addToCart(testGames[2]) // 39.99
            
            val expectedTotal = 59.99 + 49.99 + 39.99
            cart.getTotalPrice().shouldBe(expectedTotal)
        }
        
        it("Debería calcular total con cantidades múltiples") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0]) // 59.99 x 1
            cart.addToCart(testGames[0]) // 59.99 x 2
            cart.addToCart(testGames[1]) // 49.99 x 1
            
            val expectedTotal = (59.99 * 2) + 49.99
            cart.getTotalPrice().shouldBe(expectedTotal)
        }
    }
    
    describe("ShoppingCartRepository - Decrementar cantidad") {
        
        it("Debería decrementar cantidad de producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[0])
            
            cart.decreaseQuantity(testGames[0].id)
            cart.getCartItems().first().quantity.shouldBe(2)
        }
        
        it("Debería eliminar producto cuando cantidad llega a 0") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.decreaseQuantity(testGames[0].id)
            
            cart.getCartCount().shouldBe(0)
            cart.getCartItems().shouldBeEmpty()
        }
        
        it("Debería mantener otros productos al decrementar") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[1])
            
            cart.decreaseQuantity(testGames[0].id)
            
            cart.getCartCount().shouldBe(2) // 1 de game 0 + 1 de game 1
            cart.getCartItems().size.shouldBe(2)
        }
    }
    
    describe("ShoppingCartRepository - Remover productos") {
        
        it("Debería remover completamente un producto del carrito") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[1])
            
            cart.removeFromCart(testGames[0].id)
            
            cart.getCartCount().shouldBe(1)
            cart.getCartItems().first().game.id.shouldBe(2)
        }
        
        it("Debería no fallar al remover producto inexistente") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.removeFromCart(999) // ID inexistente
            
            cart.getCartCount().shouldBe(1)
        }
    }
    
    describe("ShoppingCartRepository - Limpiar carrito") {
        
        it("Debería vaciar el carrito completamente") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[1])
            cart.addToCart(testGames[2])
            
            cart.clearCart()
            
            cart.getCartCount().shouldBe(0)
            cart.getTotalPrice().shouldBe(0.0)
            cart.getCartItems().shouldBeEmpty()
        }
    }
    
    describe("ShoppingCartRepository - Obtener carrito") {
        
        it("Debería retornar lista de items correctamente ordenados") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[2])
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[1])
            
            val cartItems = cart.getCartItems()
            
            cartItems.size.shouldBe(3)
            cartItems[0].game.id.shouldBe(3)
            cartItems[1].game.id.shouldBe(1)
            cartItems[2].game.id.shouldBe(2)
        }
        
        it("Debería retornar carrito vacío cuando no hay productos") {
            val cart = ShoppingCartRepository()
            
            cart.getCartItems().shouldBeEmpty()
        }
    }
    
    describe("ShoppingCartRepository - Cálculos financieros") {
        
        it("Debería calcular correctamente con precios decimales") {
            val cart = ShoppingCartRepository()
            
            val game1 = testGames[0].copy(price = 19.99)
            val game2 = testGames[1].copy(price = 29.99)
            
            cart.addToCart(game1)
            cart.addToCart(game2)
            
            val expectedTotal = 19.99 + 29.99
            val result = cart.getTotalPrice()
            val tolerance = 0.001
            
            (kotlin.math.abs(result - expectedTotal) < tolerance).shouldBe(true)
        }
        
        it("Debería manejar precios muy altos") {
            val cart = ShoppingCartRepository()
            
            val expensiveGame = testGames[0].copy(price = 999.99)
            cart.addToCart(expensiveGame)
            cart.addToCart(expensiveGame)
            
            val expectedTotal = 1999.98
            val result = cart.getTotalPrice()
            val tolerance = 0.01
            
            (kotlin.math.abs(result - expectedTotal) < tolerance).shouldBe(true)
        }
        
        it("Debería manejar cantidades grandes") {
            val cart = ShoppingCartRepository()
            
            // Agregar el mismo producto 100 veces
            repeat(100) {
                cart.addToCart(testGames[0])
            }
            
            // getCartCount() retorna el número de items diferentes, no la cantidad total
            cart.getCartCount().shouldBe(1)
            cart.getCartItems().first().quantity.shouldBe(100)
            
            val expectedTotal = 59.99 * 100
            val result = cart.getTotalPrice()
            val tolerance = 0.1
            
            (kotlin.math.abs(result - expectedTotal) < tolerance).shouldBe(true)
        }
    }
    
    describe("ShoppingCartRepository - Mensajes") {
        
        it("Debería generar mensaje al agregar producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.lastActionMessage?.contains("Agregado").shouldBe(true)
        }
        
        it("Debería generar mensaje al remover producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            val messageBefore = cart.lastActionMessage
            cart.removeFromCart(testGames[0].id)
            
            messageBefore?.contains("Agregado").shouldBe(true)
        }
        
        it("Debería actualizar mensaje al agregar nuevo producto") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            val firstMessage = cart.lastActionMessage
            
            cart.addToCart(testGames[1])
            val secondMessage = cart.lastActionMessage
            
            firstMessage?.contains("Elden Ring").shouldBe(true)
            secondMessage?.contains("Cyberpunk 2077").shouldBe(true)
        }
    }
    
    describe("ShoppingCartRepository - Estado") {
        
        it("Debería indicar si el carrito está vacío") {
            val cart = ShoppingCartRepository()
            
            cart.getCartCount().shouldBe(0)
            
            cart.addToCart(testGames[0])
            cart.getCartCount().shouldBe(1)
        }
        
        it("Debería mantener estado consistente") {
            val cart = ShoppingCartRepository()
            
            cart.addToCart(testGames[0])
            cart.addToCart(testGames[1])
            val count1 = cart.getCartCount()
            
            cart.removeFromCart(testGames[0].id)
            val count2 = cart.getCartCount()
            
            count1.shouldBe(2)
            count2.shouldBe(1)
        }
    }
})
