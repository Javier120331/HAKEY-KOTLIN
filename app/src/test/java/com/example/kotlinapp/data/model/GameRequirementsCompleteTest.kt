package com.example.kotlinapp.data.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class GameRequirementsCompleteTest : DescribeSpec({
    
    describe("GameRequirements - Creación y propiedades") {
        
        it("Debería crear GameRequirements con valores por defecto") {
            val requirements = GameRequirements()
            
            requirements.os.shouldBe("")
            requirements.processor.shouldBe("")
            requirements.memory.shouldBe("")
            requirements.graphics.shouldBe("")
            requirements.storage.shouldBe("")
        }
        
        it("Debería crear GameRequirements con valores específicos para PC") {
            val requirements = GameRequirements(
                os = "Windows 10 64-bit",
                processor = "Intel Core i5-8400",
                memory = "8 GB RAM",
                graphics = "NVIDIA GTX 1060",
                storage = "100 GB SSD"
            )
            
            requirements.os.shouldBe("Windows 10 64-bit")
            requirements.processor.shouldBe("Intel Core i5-8400")
            requirements.memory.shouldBe("8 GB RAM")
            requirements.graphics.shouldBe("NVIDIA GTX 1060")
            requirements.storage.shouldBe("100 GB SSD")
        }
    }
    
    describe("GameRequirements - Validación de contenido") {
        
        it("Debería contener información de OS") {
            val requirements = GameRequirements(
                os = "Windows 10 64-bit"
            )
            
            requirements.os.shouldContain("Windows")
        }
        
        it("Debería contener información de procesador") {
            val requirements = GameRequirements(
                processor = "Intel Core i7-9700K"
            )
            
            requirements.processor.shouldContain("Intel")
        }
        
        it("Debería contener información de memoria") {
            val requirements = GameRequirements(
                memory = "16 GB RAM"
            )
            
            requirements.memory.shouldContain("GB")
        }
        
        it("Debería contener información de gráficos") {
            val requirements = GameRequirements(
                graphics = "NVIDIA GeForce RTX 3080"
            )
            
            requirements.graphics.shouldContain("NVIDIA")
        }
        
        it("Debería contener información de almacenamiento") {
            val requirements = GameRequirements(
                storage = "150 GB SSD"
            )
            
            requirements.storage.shouldContain("SSD")
        }
    }
    
    describe("GameRequirements - Requisitos mínimos vs recomendados") {
        
        it("Debería permitir crear requisitos mínimos") {
            val minRequirements = GameRequirements(
                os = "Windows 7 64-bit",
                processor = "Intel Core i3",
                memory = "4 GB RAM",
                graphics = "NVIDIA GTX 960",
                storage = "50 GB HDD"
            )
            
            minRequirements.memory.shouldBe("4 GB RAM")
        }
        
        it("Debería permitir crear requisitos recomendados") {
            val recRequirements = GameRequirements(
                os = "Windows 10 64-bit",
                processor = "Intel Core i7",
                memory = "16 GB RAM",
                graphics = "NVIDIA RTX 3080",
                storage = "150 GB SSD"
            )
            
            recRequirements.memory.shouldBe("16 GB RAM")
        }
    }
    
    describe("GameRequirements - Copiar y modificar") {
        
        it("Debería permitir copiar GameRequirements") {
            val req1 = GameRequirements(
                os = "Windows 10",
                processor = "Intel i5",
                memory = "8 GB"
            )
            
            val req2 = req1.copy(memory = "16 GB")
            
            req1.memory.shouldBe("8 GB")
            req2.memory.shouldBe("16 GB")
            req2.os.shouldBe("Windows 10")
        }
    }
})
