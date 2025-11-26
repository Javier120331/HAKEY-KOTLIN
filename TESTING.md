# TESTING.md - Guía de Pruebas Unitarias

Suite completa de pruebas unitarias y UI utilizando las mejores prácticas en testing de Android.

## 📋 Descripción General

La aplicación incluye pruebas con las siguientes dependencias:

- **Kotest** - Framework de testing moderno para Kotlin
- **JUnit 5** - Framework de testing estándar
- **MockK** - Librería de mocking especializada para Kotlin
- **Coroutines Test** - Utilidades para testing asincrónico
- **Compose UI Test** - Framework para testing de interfaces Compose

## 📁 Estructura de Pruebas

```
app/src/
├── test/java/com/example/kotlinapp/          ← Tests Unitarios (sin Android Runtime)
│   ├── data/
│   │   ├── api/
│   │   │   ├── ApiServicesErrorHandlingTest.kt
│   │   │   ├── GameApiServiceTest.kt
│   │   │   ├── RetrofitClientTest.kt
│   │   │   └── UserApiServiceTest.kt
│   │   ├── model/
│   │   │   ├── CartItemAdvancedTest.kt
│   │   │   ├── CartItemEdgeCasesTest.kt
│   │   │   └── GameEdgeCasesTest.kt
│   │   └── repository/
│   │       ├── GameRepositoryTest.kt
│   │       └── ShoppingCartRepositoryTest.kt
│   └── ui/
│       └── navigation/
│           └── [Otros tests]
│
└── androidTest/java/com/example/kotlinapp/   ← Tests UI (en Android Runtime)
    └── ui/
        └── screens/
            ├── LoginScreenTest.kt              # Tests Compose para login
            └── MainHomeScreenTest.kt           # Tests Compose para home
```

## 🧪 Tipos de Pruebas

### 1. Tests Unitarios (`src/test/`)

Validan lógica de negocio sin necesidad de Android Runtime.

#### GameRepositoryTest.kt

Utiliza **Kotest** y **MockK** en estilo **BehaviorSpec**:

```kotlin
Given("Un GameRepository con API service mockeado") {
    When("Se llama a getGames()") {
        Then("Debería retornar la lista de juegos")
    }
}
```

**Pruebas incluidas:**

- ✅ Obtener lista de juegos desde API
- ✅ Obtener juego por ID
- ✅ Crear nuevos juegos
- ✅ Actualizar juegos existentes
- ✅ Eliminar juegos
- ✅ Manejo de errores (fallback a datos locales)

#### ShoppingCartRepositoryTest.kt

Utiliza **Kotest** en estilo **DescribeSpec**:

```kotlin
describe("ShoppingCartRepository - Agregar productos") {
    it("Debería agregar un juego al carrito")
    it("Debería incrementar cantidad si existe")
    it("Debería calcular total correctamente")
}
```

**Pruebas incluidas:**

- ✅ Agregar juegos al carrito
- ✅ Aumentar cantidad de productos existentes
- ✅ Remover productos del carrito
- ✅ Disminuir cantidad
- ✅ Cálculo automático de total
- ✅ Validación de mensajes

#### GameApiServiceTest.kt

Tests de integración de Retrofit con **MockK**:

**Pruebas incluidas:**

- ✅ Respuestas exitosas (200 OK)
- ✅ Validación de códigos de error HTTP (400, 404, 500)
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ Actualización parcial (PATCH)
- ✅ Parsing correcto de JSON

#### UserApiServiceTest.kt

Tests para autenticación y usuarios:

**Pruebas incluidas:**

- ✅ Obtener lista de usuarios
- ✅ Crear usuarios (registro)
- ✅ Login y autenticación
- ✅ Actualizar perfil
- ✅ Manejo de errores de autenticación

#### Edge Cases Tests

`CartItemEdgeCasesTest.kt`, `GameEdgeCasesTest.kt`:

**Pruebas incluidas:**

- ✅ Valores null
- ✅ Strings vacíos
- ✅ Números negativos
- ✅ Listas vacías
- ✅ Desbordamiento de datos

### 2. Tests UI (`src/androidTest/`)

Validan el comportamiento de pantallas Compose en Android Runtime (requiere emulador/dispositivo).

#### LoginScreenTest.kt

Utiliza **Compose UI Test**:

```kotlin
@OptIn(ExperimentalTestApi::class)
@Test
fun loginScreen_should_display_all_required_elements() = runComposeUiTest {
    setContent { LoginScreen() }
    onNodeWithText("Login").assertIsDisplayed()
}
```

**Pruebas incluidas:**

- ✅ Visualización de elementos UI (botones, inputs, textos)
- ✅ Validación de email
- ✅ Validación de contraseña
- ✅ Mensajes de error
- ✅ Navegación a registro
- ✅ Estado de carga (loading indicators)
- ✅ Interacciones de usuario (clicks, text input)

#### MainHomeScreenTest.kt

Tests de la pantalla principal:

**Pruebas incluidas:**

- ✅ Mostrar lista de juegos
- ✅ Agregar juegos al carrito
- ✅ Mostrar múltiples productos
- ✅ Actualizar carrito
- ✅ Búsqueda y filtrado

## 🔧 Configuración de Dependencias

Dependencias en `app/build.gradle`:

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

## 🚀 Cómo Ejecutar Pruebas

### Ejecutar Todos los Tests Unitarios

```bash
./gradlew test
```

### Ejecutar Todos los Tests UI

```bash
./gradlew connectedAndroidTest
# Requiere emulador o dispositivo conectado
```

### Ejecutar Tests Específicos

```bash
# Un archivo de test específico
./gradlew test --tests "*GameRepositoryTest*"

# Múltiples archivos
./gradlew test --tests "*Repository*"

# Un método específico
./gradlew test --tests "*GameRepositoryTest.test_should_return_games*"
```

### Ejecutar con Cobertura de Código

```bash
./gradlew test --tests "*" --coverage
```

### Ejecutar en Debug Mode

```bash
./gradlew test --debug
```

## 📚 Patrones de Testing

### 1. Mocking con MockK

```kotlin
val mockApiService = mockk<GameApiService>()

// Mock respuesta exitosa
coEvery { mockApiService.getGames() } returns Response.success(mockGames)

// Mock excepción
coEvery { mockApiService.getGames() } throws IOException("Network error")

// Verify calls
coVerify { mockApiService.getGames() }
coVerify(exactly = 2) { mockApiService.getGames() }
```

### 2. Testing Asincrónico con Coroutines-Test

```kotlin
@Test
fun gameRepository_should_return_games_when_api_succeeds() = runTest {
    val result = repository.getGames()
    result.shouldHaveSize(2)
    result[0].title.shouldBe("Elden Ring")
}
```

### 3. Assertions con Kotest

```kotlin
// Colecciones
result.shouldHaveSize(2)
result.shouldContain(game)
result.shouldBeEmpty()

// Strings
message.shouldContain("error")
message.shouldStartWith("Loading")
message.shouldMatch(Regex("[A-Z]+"))

// Valores
price.shouldBe(59.99)
price.shouldBeGreaterThan(0)
```

### 4. UI Testing con Compose

```kotlin
// Buscar elementos
onNodeWithText("Login").assertIsDisplayed()
onNodeWithTag("emailInput").performTextInput("test@example.com")

// Acciones
onNodeWithText("Sign in").performClick()
onNodeWithText("Password").performTextClearance()

// Assertions
onNodeWithText("Error").assertIsDisplayed()
onNodeWithTag("loadingSpinner").assertExists()
```

## ✨ Convenciones de Nombres

Todos los tests siguen convenciones descriptivas:

**Archivo:** `[Class]Test.kt`

- Ej: `GameRepositoryTest.kt`

**Método:** `[subjectUnderTest]_should_[expectedBehavior]_when_[condition]()`

- Ej: `gameRepository_should_return_games_when_api_succeeds()`

**Variables:**

- `subject` - Objeto a probar
- `result` - Resultado de la operación
- `expected` - Valor esperado

Ejemplo completo:

```kotlin
@Test
fun shoppingCartRepository_should_increase_quantity_when_adding_same_game() = runTest {
    // Given
    val subject = ShoppingCartRepository()
    val game = Game(id = "1", title = "Elden Ring", price = 59.99, ...)

    // When
    subject.addItem(CartItem(game, quantity = 1))
    subject.addItem(CartItem(game, quantity = 1))

    // Then
    val result = subject.getItems()
    result.shouldHaveSize(1)
    result[0].quantity.shouldBe(2)
}
```

## 🎯 Cobertura de Código

Objetivos de cobertura:

| Componente   | Cobertura Objetivo |
| ------------ | ------------------ |
| Repositorios | > 90%              |
| APIs         | > 85%              |
| Modelos      | > 80%              |
| UI Logic     | > 75%              |
| **Total**    | **> 80%**          |

Para revisar cobertura:

```bash
./gradlew test jacoco
# Ver reporte en: app/build/reports/jacoco/test/
```

## 🔍 Debugging de Tests

### Ver logs en tests

```kotlin
println("Debug info: $value")
// O usar logger
logger.info("Test debug message")
```

### Pausar en breakpoint

1. Coloca breakpoint en el test
2. Ejecuta: `./gradlew test --debug`
3. Android Studio se pausará en breakpoints

### Ejecutar test específico en debug

```kotlin
@Test
@Disabled("Only for debugging")
fun debug_test_specific_feature() { ... }

// Luego ejecutar solo ese
./gradlew test --tests "*debug*"
```

## 📊 Resultados de Tests

Después de ejecutar tests, revisa:

**Reporte de texto:**

```
app/build/test-results/test/
  ├── TEST-com.example.kotlinapp.data.repository.GameRepositoryTest.xml
  └── TEST-*.xml
```

**En Android Studio:**

- View → Tool Windows → Gradle → app → Tasks → verification → test
- Ver resultados en ventana de ejecución

## ✅ Checklist Pre-Commit

Antes de hacer commit:

- [ ] Ejecutar `./gradlew test`
- [ ] Ejecutar `./gradlew connectedAndroidTest`
- [ ] Todos los tests pasan (green)
- [ ] No hay warnings de test
- [ ] Cobertura > 80%

## 🔗 Referencias

- [Kotest Documentation](https://kotest.io/docs/)
- [MockK Documentation](https://mockk.io/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Compose Testing Docs](https://developer.android.com/jetpack/compose/testing)
- [Kotlin Coroutines Testing](https://kotlinlang.org/docs/kotlinx-coroutines-test.html)

---

**Última actualización:** Noviembre 2025
