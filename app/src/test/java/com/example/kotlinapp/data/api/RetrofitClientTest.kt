package com.example.kotlinapp.data.api

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf

class RetrofitClientTest : DescribeSpec({
    
    describe("RetrofitClient - Inicialización") {
        
        it("Debería inicializar GameApiService correctamente") {
            val gameApiService = RetrofitClient.gameApiService
            
            gameApiService.shouldNotBe(null)
            gameApiService.shouldBeInstanceOf<GameApiService>()
        }
        
        it("Debería inicializar UserApiService correctamente") {
            val userApiService = RetrofitClient.userApiService
            
            userApiService.shouldNotBe(null)
            userApiService.shouldBeInstanceOf<UserApiService>()
        }
    }
    
    describe("RetrofitClient - Configuración") {
        
        it("Debería tener un cliente Retrofit configurado") {
            // Verificar que los servicios están disponibles (esto indica que Retrofit está configurado)
            val gameService = RetrofitClient.gameApiService
            val userService = RetrofitClient.userApiService
            
            gameService.shouldNotBe(null)
            userService.shouldNotBe(null)
        }
    }
    
    describe("RetrofitClient - Persistencia de instancias") {
        
        it("Debería retornar la misma instancia de GameApiService") {
            val service1 = RetrofitClient.gameApiService
            val service2 = RetrofitClient.gameApiService
            
            service1.shouldBe(service2)
        }
        
        it("Debería retornar la misma instancia de UserApiService") {
            val service1 = RetrofitClient.userApiService
            val service2 = RetrofitClient.userApiService
            
            service1.shouldBe(service2)
        }
    }
})
