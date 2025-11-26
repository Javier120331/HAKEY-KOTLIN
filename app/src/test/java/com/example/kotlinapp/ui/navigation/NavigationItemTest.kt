package com.example.kotlinapp.ui.navigation

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize

class NavigationItemTest : DescribeSpec({
    
    describe("NavigationItem - Propiedades") {
        
        it("Debería tener route 'catalog' para Catalog") {
            NavigationItem.Catalog.route.shouldBe("catalog")
        }
        
        it("Debería tener title 'Catálogo' para Catalog") {
            NavigationItem.Catalog.title.shouldBe("Catálogo")
        }
        
        it("Debería tener route 'cart' para Cart") {
            NavigationItem.Cart.route.shouldBe("cart")
        }
        
        it("Debería tener title 'Carrito' para Cart") {
            NavigationItem.Cart.title.shouldBe("Carrito")
        }
        
        it("Debería tener route 'account' para Account") {
            NavigationItem.Account.route.shouldBe("account")
        }
        
        it("Debería tener title 'Mi Cuenta' para Account") {
            NavigationItem.Account.title.shouldBe("Mi Cuenta")
        }
    }
    
    describe("NavigationItem - Iconos") {
        
        it("Debería tener un icono para Catalog") {
            NavigationItem.Catalog.icon.shouldBe(NavigationItem.Catalog.icon)
        }
        
        it("Debería tener un icono para Cart") {
            NavigationItem.Cart.icon.shouldBe(NavigationItem.Cart.icon)
        }
        
        it("Debería tener un icono para Account") {
            NavigationItem.Account.icon.shouldBe(NavigationItem.Account.icon)
        }
    }
    
    describe("navigationItems - Lista de navegación") {
        
        it("Debería contener exactamente 3 items") {
            navigationItems.shouldHaveSize(3)
        }
        
        it("Debería contener Catalog") {
            navigationItems.shouldContain(NavigationItem.Catalog)
        }
        
        it("Debería contener Cart") {
            navigationItems.shouldContain(NavigationItem.Cart)
        }
        
        it("Debería contener Account") {
            navigationItems.shouldContain(NavigationItem.Account)
        }
        
        it("Debería estar en el orden correcto") {
            navigationItems[0].shouldBe(NavigationItem.Catalog)
            navigationItems[1].shouldBe(NavigationItem.Cart)
            navigationItems[2].shouldBe(NavigationItem.Account)
        }
    }
})
