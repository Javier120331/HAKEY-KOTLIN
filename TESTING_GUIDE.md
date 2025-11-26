# Guía de Pruebas Unitarias - HAKEY-KOTLIN

## 📋 Descripción General

Este proyecto incluye una suite completa de pruebas unitarias, de integración y UI utilizando las siguientes dependencias:

- **Kotest**: Framework de testing moderno y flexible para Kotlin
- **JUnit 5**: Framework de testing estándar en Java/Kotlin
- **MockK**: Librería de mocking especializada para Kotlin
- **Coroutines Test**: Utilidades para testear código asincrónico
- **Compose UI Test**: Framework para testear interfaces Compose

## 📁 Estructura de las Pruebas

```
app/src/
├── test/java/com/example/kotlinapp/          (Pruebas Unitarias)
│   ├── data/
│   │   ├── repository/
│   │   │   ├── GameRepositoryTest.kt         # Tests del repositorio de juegos
│   │   │   └── ShoppingCartRepositoryTest.kt # Tests del repositorio del carrito
│   │   └── api/
│   │       ├── GameApiServiceTest.kt         # Tests del servicio API de juegos
│   │       └── UserApiServiceTest.kt         # Tests del servicio API de usuarios
│   │
│   └── [Otros tests unitarios aquí]
│
└── androidTest/java/com/example/kotlinapp/   (Pruebas UI)
    └── ui/
        └── screens/
            ├── LoginScreenTest.kt              # Tests UI para pantalla de login
            └── MainHomeScreenTest.kt           # Tests UI para pantalla principal
```

## 🧪 Tipos de Pruebas

### 1. **Pruebas Unitarias** (`src/test/`)

Estos tests validan la lógica de negocio sin necesidad de Android Runtime.

#### GameRepositoryTest.kt
Utiliza **Kotest** y **MockK** para probar:
- ✅ Obtener lista de juegos desde API
- ✅ Obtener juego por ID
- ✅ Crear nuevos juegos
- ✅ Actualizar juegos existentes
- ✅ Eliminar juegos
- ✅ Manejo de errores de API (fallback a datos locales)

**Estilo**: BehaviorSpec (Given-When-Then)

```kotlin
Given("Un GameRepository con API service mockeado") {
    When("Se llama a getGames()") {
        Then("Debería retornar la lista de juegos")
    }
}
```

#### ShoppingCartRepositoryTest.kt
Utiliza **Kotest** para probar:
- ✅ Agregar juegos al carrito
- ✅ Aumentar cantidad de productos
- ✅ Remover productos del carrito
- ✅ Disminuir cantidad
- ✅ Cálculo total de precio
- ✅ Mensajes de acción

**Estilo**: DescribeSpec (Describe-It)

```kotlin
describe("ShoppingCartRepository - Agregar productos") {
    it("Debería agregar un juego al carrito")
}
```

#### GameApiServiceTest.kt
Utiliza **MockK** para mockear Retrofit:
- ✅ Validar respuestas exitosas
- ✅ Validar códigos de error HTTP
- ✅ Probar CRUD completo (Create, Read, Update, Delete)
- ✅ Patching parcial de datos

#### UserApiServiceTest.kt
Pruebas para autenticación y usuarios:
- ✅ Obtener usuarios
- ✅ Crear usuarios (registro)
- ✅ Login y autenticación
- ✅ Actualizar perfil
- ✅ Manejo de errores de autenticación

### 2. **Pruebas UI** (`src/androidTest/`)

Estos tests validan el comportamiento de las interfaces Compose.

#### LoginScreenTest.kt
Utiliza **Compose UI Test** para validar:
- ✅ Visualización de elementos UI
- ✅ Validación de email
- ✅ Validación de contraseña
- ✅ Mensajes de error
- ✅ Navegación a registro
- ✅ Estado de carga

**Patrón de prueba**:
```kotlin
@OptIn(ExperimentalTestApi::class)
@Test
fun loginScreen_should_display_all_required_elements() = runComposeUiTest {
    setContent { /* Compose content */ }
    onNodeWithText("Login").assertIsDisplayed()
}
```

#### MainHomeScreenTest.kt
Pruebas para la pantalla principal:
- ✅ Mostrar lista de juegos
- ✅ Agregar juegos al carrito
- ✅ Mostrar múltiples productos

## 🔧 Configuración de Dependencias

Las siguientes dependencias han sido agregadas a `app/build.gradle`:

```gradle
// JUnit 5
testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.3'
testImplementation 'org.junit.jupiter:junit-jupiter-params:5.9.3'
testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.3'
testImplementation 'de.mannodermaus.gradle.plugins:android-junit5:1.9.1.0'

// Kotest
testImplementation 'io.kotest:kotest-runner-junit5:5.7.2'
testImplementation 'io.kotest:kotest-assertions-core:5.7.2'
testImplementation 'io.kotest:kotest-property:5.7.2'

// MockK
testImplementation 'io.mockk:mockk:1.13.7'

// Coroutines Test
testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1'

// Compose UI Tests
androidTestImplementation 'androidx.compose.ui:ui-test-junit4:1.6.0'
debugImplementation 'androidx.compose.ui:ui-test-manifest:1.6.0'
androidTestImplementation 'androidx.compose.ui:ui-test:1.6.0'
```

## 🚀 Cómo Ejecutar las Pruebas

### Ejecutar todas las pruebas
```bash
./gradlew test                    # Tests unitarios
./gradlew connectedAndroidTest    # Tests UI (requiere emulador/dispositivo)
```

### Ejecutar un test específico
```bash
./gradlew test --tests "*GameRepositoryTest*"
./gradlew test --tests "*ShoppingCartRepositoryTest*"
```

### Ejecutar con cobertura
```bash
./gradlew test --tests "*" --coverage
```

## 📚 Patrones de Testing Utilizados

### 1. **Mocking con MockK**
```kotlin
val mockApiService = mockk<GameApiService>()
coEvery { mockApiService.getGames() } returns Response.success(mockGames)
```

### 2. **Testing Asincrónico con Coroutines-Test**
```kotlin
runTest {
    val result = repository.getGames()
    result.shouldBe(mockGames)
}
```

### 3. **Assertions con Kotest**
```kotlin
result.shouldHaveSize(2)
result[0].title.shouldBe("Elden Ring")
```

### 4. **UI Testing con Compose**
```kotlin
onNodeWithText("Login").assertIsDisplayed()
onNodeWithText("Email").performTextInput("test@example.com")
```

## ✨ Nombres Descriptivos

Todos los tests siguen la convención de nombres descriptiva:
- `[Class]Test.kt` para archivo de test
- `test_should_[expected_behavior]_when_[condition]()` para métodos
- Ejemplo: `gameRepository_should_return_games_when_api_succeeds()`

## 🎯 Próximos Pasos

Para mantener la calidad del código:

1. Ejecutar tests antes de hacer commit
2. Mantener cobertura de código > 80%
3. Agregar tests para nuevas funcionalidades
4. Ejecutar tests UI en emulador Android 9+ (API 28+)

## 📖 Referencias

- [Kotest Documentation](https://kotest.io)
- [MockK Documentation](https://mockk.io)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Compose Testing Documentation](https://developer.android.com/jetpack/compose/testing)
- [Coroutines Testing Documentation](https://kotlinlang.org/docs/kotlinx-coroutines-test.html)
