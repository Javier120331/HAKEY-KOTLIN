package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GameRequirementsEdgeCasesTest : DescribeSpec({
    
    describe("GameRequirements - Casos límite") {
        
        it("Debería crear GameRequirements con valores por defecto") {
            val requirements = GameRequirements()
            
            requirements.os.shouldBe("")
            requirements.processor.shouldBe("")
            requirements.memory.shouldBe("")
            requirements.graphics.shouldBe("")
            requirements.storage.shouldBe("")
        }
        
        it("Debería manejar OS vacío") {
            val requirements = GameRequirements(os = "")
            
            requirements.os.shouldBe("")
        }
        
        it("Debería manejar OS con valor") {
            val requirements = GameRequirements(os = "Windows 10 64-bit")
            
            requirements.os.shouldBe("Windows 10 64-bit")
        }
        
        it("Debería manejar processor vacío") {
            val requirements = GameRequirements(processor = "")
            
            requirements.processor.shouldBe("")
        }
        
        it("Debería manejar processor con valor") {
            val requirements = GameRequirements(processor = "Intel Core i7-9700K")
            
            requirements.processor.shouldBe("Intel Core i7-9700K")
        }
        
        it("Debería manejar memory vacío") {
            val requirements = GameRequirements(memory = "")
            
            requirements.memory.shouldBe("")
        }
        
        it("Debería manejar memory con valor") {
            val requirements = GameRequirements(memory = "16 GB RAM")
            
            requirements.memory.shouldBe("16 GB RAM")
        }
        
        it("Debería manejar graphics vacío") {
            val requirements = GameRequirements(graphics = "")
            
            requirements.graphics.shouldBe("")
        }
        
        it("Debería manejar graphics con valor") {
            val requirements = GameRequirements(graphics = "NVIDIA GeForce RTX 3080")
            
            requirements.graphics.shouldBe("NVIDIA GeForce RTX 3080")
        }
        
        it("Debería manejar storage vacío") {
            val requirements = GameRequirements(storage = "")
            
            requirements.storage.shouldBe("")
        }
        
        it("Debería manejar storage con valor") {
            val requirements = GameRequirements(storage = "150 GB SSD")
            
            requirements.storage.shouldBe("150 GB SSD")
        }
        
        it("Debería crear GameRequirements completo") {
            val requirements = GameRequirements(
                os = "Windows 10/11 64-bit",
                processor = "Intel Core i7-9700K / AMD Ryzen 5 3600",
                memory = "16 GB RAM",
                graphics = "NVIDIA GeForce RTX 3080 / AMD Radeon RX 5700 XT",
                storage = "150 GB SSD"
            )
            
            requirements.os.shouldBe("Windows 10/11 64-bit")
            requirements.processor.shouldBe("Intel Core i7-9700K / AMD Ryzen 5 3600")
            requirements.memory.shouldBe("16 GB RAM")
            requirements.graphics.shouldBe("NVIDIA GeForce RTX 3080 / AMD Radeon RX 5700 XT")
            requirements.storage.shouldBe("150 GB SSD")
        }
        
        it("Debería permitir copiar GameRequirements") {
            val req1 = GameRequirements(
                os = "Windows 10",
                processor = "Intel i7",
                memory = "8 GB",
                graphics = "RTX 2060",
                storage = "50 GB"
            )
            
            val req2 = req1.copy(memory = "16 GB")
            
            req1.memory.shouldBe("8 GB")
            req2.memory.shouldBe("16 GB")
            req2.os.shouldBe("Windows 10")
        }
        
        it("Debería detectar GameRequirements iguales") {
            val req1 = GameRequirements(
                os = "Windows 10",
                processor = "Intel i7",
                memory = "8 GB",
                graphics = "RTX 2060",
                storage = "50 GB"
            )
            
            val req2 = GameRequirements(
                os = "Windows 10",
                processor = "Intel i7",
                memory = "8 GB",
                graphics = "RTX 2060",
                storage = "50 GB"
            )
            
            req1.shouldBe(req2)
        }
        
        it("Debería manejar strings muy largos") {
            val longString = "A".repeat(500)
            val requirements = GameRequirements(
                os = longString,
                processor = longString,
                memory = longString,
                graphics = longString,
                storage = longString
            )
            
            requirements.os.length.shouldBe(500)
            requirements.processor.length.shouldBe(500)
            requirements.memory.length.shouldBe(500)
            requirements.graphics.length.shouldBe(500)
            requirements.storage.length.shouldBe(500)
        }
    }
})
