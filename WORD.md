# HAKEY - Guía de Defensa del Proyecto

## Tabla de Contenidos

1. [Preguntas sobre la Arquitectura General](#arquitectura-general)
2. [Preguntas sobre Composables y UI](#composables-y-ui)
3. [Preguntas sobre Layout y Positioning](#layout-y-positioning)
4. [Preguntas sobre Estado y Reactividad](#estado-y-reactividad)
5. [Preguntas sobre APIs y Networking](#apis-y-networking)
6. [Preguntas sobre Repositorios y Datos](#repositorios-y-datos)
7. [Preguntas sobre Testing](#testing)
8. [Preguntas sobre Gradle y Build](#gradle-y-build)
9. [Preguntas sobre Seguridad](#seguridad)
10. [Preguntas sobre Firma de APK](#firma-de-apk)

---

## ARQUITECTURA GENERAL

### ¿Cuál es la arquitectura general del proyecto?

**Respuesta:**
El proyecto HAKEY utiliza una arquitectura en capas basada en MVVM (Model-View-ViewModel):

- **Data Layer**: Contiene los repositorios y servicios de API

  - `GameRepository.kt` - Gestiona datos de juegos
  - `UserRepository.kt` - Gestiona datos de usuarios
  - `ShoppingCartRepository.kt` - Gestiona el carrito de compras
  - APIs con Retrofit para comunicarse con el backend

- **UI Layer**: Contiene todas las pantallas y composables

  - `MainHomeScreen.kt` - Pantalla principal
  - `LoginScreen.kt`, `RegisterScreen.kt` - Autenticación
  - `OtherScreens.kt` - Pantallas adicionales (Perfil, Carrito, Ofertas)

- **Model Layer**: Clases de datos
  - `Game.kt` - Modelo de juego
  - `User.kt` - Modelo de usuario
  - `CartItem.kt` - Modelo de artículo del carrito

### ¿Por qué elegiste esta arquitectura?

**Respuesta:**
Elegí MVVM porque:

- **Separación de responsabilidades**: Cada capa tiene un rol específico
- **Reutilización de código**: Los repositorios pueden ser reutilizados desde múltiples pantallas
- **Testabilidad**: Cada componente puede ser testeado de forma independiente
- **Mantenibilidad**: Es fácil agregar nuevas funcionalidades sin afectar código existente

### ¿Qué es Jetpack Compose?

**Respuesta:**
Jetpack Compose es el toolkit moderno de Android para construir interfaces de usuario:

- **Declarativo**: Describes cómo debería verse la UI, no cómo construirla paso a paso
- **Reactivo**: La UI se actualiza automáticamente cuando los datos cambian
- **Basado en Composables**: Pequeños bloques reutilizables de UI
- **Tipo seguro**: Es código Kotlin compilado, no XML

### ¿Cuál es la diferencia entre XML layout y Compose?

**Respuesta:**

| XML Layout              | Jetpack Compose             |
| ----------------------- | --------------------------- |
| Declarativo en XML      | Declarativo en Kotlin       |
| Se carga en runtime     | Se compila junto al código  |
| Mayor verbosidad        | Código más conciso          |
| Más archivos separados  | Todo en código Kotlin       |
| State management manual | State management automático |

---

## COMPOSABLES Y UI

### ¿Qué es un Composable?

**Respuesta:**
Un Composable es una función Kotlin anotada con `@Composable` que describe una parte de la UI. Características:

- Se ejecuta cuando los datos que observa cambian
- Retorna `Unit` (no retorna nada)
- Es una función pura idealmente (mismos inputs = mismos outputs)
- Ejemplo en nuestro proyecto: `CartScreen()`, `LoginScreen()`

### ¿Cómo funciona MainHomeScreen?

**Respuesta:**
`MainHomeScreen` es la pantalla principal que contiene:

```kotlin
@Composable
fun MainHomeScreen(
    userRepository: UserRepository,
    onNavigateToLogin: () -> Unit
)
```

Componentes principales:

- **TopAppBar**: Barra superior con el nombre "HAKEY" y menú hamburgesa
- **ModalNavigationDrawer**: Menú lateral (hamburguesa)
  - Muestra la foto de perfil del usuario
  - Menú de navegación
  - Botón de logout
- **Scaffold**: Contenedor principal que organiza todos los elementos
- **NavigationBar**: Barra inferior con pestañas
- **SnackbarHost**: Para mostrar notificaciones

### ¿Qué es ModalNavigationDrawer?

**Respuesta:**
Es un componente que proporciona un menú lateral deslizable:

- `drawerState`: Controla si está abierto o cerrado
- `drawerContent`: Lo que se muestra dentro del drawer
- Se abre/cierra con el botón de menú hamburgesa
- En nuestro proyecto, el drawer contiene:
  - Avatar con foto de perfil del usuario
  - Email del usuario
  - Navegación a diferentes pantallas
  - Botón de logout

### ¿Cómo se implementa la navegación entre pantallas?

**Respuesta:**
La navegación se maneja mediante estado booleano:

```kotlin
var selectedTab by remember { mutableStateOf("catalog") }

// Cambiar tab
when (selectedTab) {
    "catalog" -> CatalogScreen(shoppingCart)
    "cart" -> CartScreen(shoppingCart)
    "account" -> AccountScreen(userRepository)
    "offers" -> OffersScreen()
}
```

Componentes usados:

- **State**: `selectedTab` mantiene la pestaña actual
- **Callbacks**: `onNavigateToRoute` cambia el estado
- **When expression**: Mostrar la pantalla correcta según el estado

### ¿Qué es DrawerContent y para qué sirve?

**Respuesta:**
`DrawerContent` es un Composable que renderiza el contenido del menú lateral:

```kotlin
@Composable
fun DrawerContent(
    userEmail: String,
    profileImageUrl: String?,
    selectedTab: String,
    onNavigateToRoute: (String) -> Unit,
    onLogout: () -> Unit
)
```

Proporciona:

- Avatar redondo con la foto de perfil (o inicial si no hay foto)
- Email del usuario
- Lista de navegación items (Catálogo, Carrito, Cuenta, Ofertas)
- Botón de logout
- Visual feedback del item seleccionado

### ¿Cómo se muestra la foto de perfil en el drawer?

**Respuesta:**

```kotlin
if (!profileImageUrl.isNullOrEmpty()) {
    AsyncImage(
        model = profileImageUrl,
        contentDescription = "Foto de perfil",
        modifier = Modifier
            .size(56.dp)
            .background(...),
        contentScale = ContentScale.Crop
    )
} else {
    // Mostrar inicial si no hay foto
}
```

Usa `AsyncImage` de Coil para:

- Cargar la imagen de forma asincrónica
- No bloquear la UI mientras carga
- Cachear la imagen en memoria

---

## LAYOUT Y POSITIONING

### ¿Qué es Box en Compose?

**Respuesta:**
`Box` es un layout que apila elementos uno sobre otro:

- Puede contener múltiples hijos
- Los elementos se posicionan en las mismas coordenadas
- Útil para crear capas, overlays, etc.
- Soporta `contentAlignment` para centrado

**Ejemplo en nuestro proyecto:**

```kotlin
Box(
    modifier = Modifier
        .size(56.dp)
        .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(28.dp)),
    contentAlignment = Alignment.Center
) {
    Text(text = "J") // Avatar con inicial
}
```

### ¿Qué es Column en Compose?

**Respuesta:**
`Column` es un layout que organiza elementos verticalmente:

- Cada elemento se coloca debajo del anterior
- Soporta `verticalArrangement` para espaciado
- Soporta `horizontalAlignment` para alineación
- Ideal para formularios y listas verticales

**Ejemplo:**

```kotlin
Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text("Título")
    Button(onClick = {}) { Text("Botón") }
    TextField(value = "", onValueChange = {})
}
```

### ¿Qué es Row en Compose?

**Respuesta:**
`Row` es un layout que organiza elementos horizontalmente:

- Cada elemento se coloca a la derecha del anterior
- Soporta `horizontalArrangement` para espaciado
- Soporta `verticalAlignment` para alineación
- Útil para botones lado a lado, avatares + texto

**Ejemplo:**

```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Text("Nombre")
    Icon(imageVector = Icons.Default.Edit)
}
```

### ¿Qué es Scaffold en Compose?

**Respuesta:**
`Scaffold` es un layout que proporciona la estructura material design básica:

- `topBar`: Barra superior
- `bottomBar`: Barra inferior
- `floatingActionButton`: Botón flotante
- `snackbarHost`: Para notificaciones
- `content`: El contenido principal

**Ejemplo en MainHomeScreen:**

```kotlin
Scaffold(
    topBar = { SmallTopAppBar(...) },
    bottomBar = { NavigationBar(...) },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    content = { /* Contenido principal */ }
)
```

### ¿Qué es Modifier y para qué se usa?

**Respuesta:**
`Modifier` es un objeto que describe cómo debe verse y comportarse un composable:

Ejemplos:

- `Modifier.fillMaxSize()` - Ocupa todo el espacio disponible
- `Modifier.padding(16.dp)` - Agrega padding interno
- `Modifier.background(Color.White)` - Establece color de fondo
- `Modifier.clickable { }` - Hace clickeable
- `Modifier.width(100.dp)` - Ancho fijo
- `Modifier.weight(1f)` - Peso proporcional en layouts

Se encadenan:

```kotlin
Modifier
    .fillMaxWidth()
    .height(48.dp)
    .padding(12.dp)
    .background(Color.Blue)
```

### ¿Cuál es la diferencia entre fillMaxSize(), fillMaxWidth() y fillMaxHeight()?

**Respuesta:**

- `fillMaxSize()`: Ocupa 100% del ancho Y alto disponible
- `fillMaxWidth()`: Ocupa 100% del ancho disponible
- `fillMaxHeight()`: Ocupa 100% del alto disponible

```kotlin
Column(modifier = Modifier.fillMaxSize()) { // Todo el espacio
    Text("Arriba")
    Spacer(modifier = Modifier.weight(1f)) // Espacio flexible
    Text("Abajo")
}
```

### ¿Qué es LazyColumn y por qué es importante?

**Respuesta:**
`LazyColumn` es similar a `RecyclerView` en Android tradicional:

- **Perezoso**: Solo renderiza items visibles
- **Eficiente**: Ahorra memoria y rendimiento
- **Scrolleable**: El contenido es automáticamente scrolleable
- **Ideal para listas largas**

**Ejemplo en CartScreen:**

```kotlin
LazyColumn(
    modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    items(cartItems) { cartItem ->
        CartItemCard(cartItem)
    }
}
```

### ¿Qué es Material3 y MaterialTheme?

**Respuesta:**
`Material3` es el sistema de diseño de Google:

- `MaterialTheme`: Proporciona colores, tipografía, formas consistentes
- `colorScheme`: Paleta de colores (primary, secondary, error, etc.)
- `typography`: Estilos de texto
- `shapes`: Formas redondeadas personalizadas

Acceso en componables:

```kotlin
Text(
    text = "Hola",
    color = MaterialTheme.colorScheme.primary,
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold
)
```

---

## ESTADO Y REACTIVIDAD

### ¿Qué es remember en Compose?

**Respuesta:**
`remember` mantiene un valor en memoria durante recomposiciones:

- Sin `remember`: el valor se recrearía cada recomposición
- Con `remember`: el valor persiste

```kotlin
var count by remember { mutableStateOf(0) }
// 'count' se mantiene incluso si MainScreen se recompone
```

### ¿Cuál es la diferencia entre remember, mutableStateOf y by?

**Respuesta:**

- **mutableStateOf()**: Crea un objeto State observable
- **remember**: Cachea el valor en memoria
- **by**: Delegado de propiedades (property delegate)

```kotlin
// Sin 'by': acceso verboso
val count = remember { mutableStateOf(0) }
count.value = 5 // Acceso mediante .value

// Con 'by': acceso limpio
var count by remember { mutableStateOf(0) }
count = 5 // Acceso directo
```

### ¿Qué es LaunchedEffect y para qué se usa?

**Respuesta:**
`LaunchedEffect` ejecuta código suspendido cuando se cumple una condición:

```kotlin
LaunchedEffect(key1) {
    // Este código se ejecuta cuando 'key1' cambia
    delay(2000) // Esperar 2 segundos
    onPaymentSuccess() // Ejecutar callback
}
```

En PaymentFormScreen:

```kotlin
LaunchedEffect(isProcessing) {
    if (isProcessing) {
        delay(2000) // Simular procesamiento de pago
        onPaymentSuccess()
    }
}
```

### ¿Cómo funciona el state hoisting?

**Respuesta:**
State hoisting es mover el estado a un nivel superior en la jerarquía:

**Antes (state en el composable):**

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) { Text(count.toString()) }
}
```

**Después (state elevado):**

```kotlin
@Composable
fun MainScreen() {
    var count by remember { mutableStateOf(0) }
    Counter(count, onCountChange = { count = it })
}

@Composable
fun Counter(count: Int, onCountChange: (Int) -> Unit) {
    Button(onClick = { onCountChange(count + 1) }) { Text(count.toString()) }
}
```

**Ventajas:**

- Reutilizable
- Testeable
- Composable puro

### ¿Cómo se actualiza la foto de perfil en el drawer?

**Respuesta:**
En `MainHomeScreen`:

```kotlin
var profileImageUrl by remember { mutableStateOf(userRepository.getProfileImageUrl()) }

// LaunchedEffect para escuchar cambios
LaunchedEffect(Unit) {
    profileImageUrl = userRepository.getProfileImageUrl()
}

// Pasar al DrawerContent
DrawerContent(
    userEmail = userEmail,
    profileImageUrl = profileImageUrl, // Se pasa aquí
    ...
)
```

Cuando el usuario cambia la foto en `AccountScreen`, se actualiza en el repositorio y `profileImageUrl` se re-renderiza automáticamente.

---

## APIS Y NETWORKING

### ¿Cómo funciona la conexión con la API?

**Respuesta:**
Usamos Retrofit para comunicarnos con el backend:

```kotlin
// RetrofitClient.kt
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:4000" // Para emulador

    val instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
```

### ¿Por qué usaste Retrofit?

**Respuesta:**

- **Simpleza**: Interfaz limpia y tipo segura
- **Anotaciones**: Declara endpoints de forma clara
- **Serialización automática**: Convierte JSON a objetos Kotlin
- **Soporte para coroutines**: Fácil integración con código asincrónico

### ¿Qué es un Interceptor en Retrofit?

**Respuesta:**
Los interceptores permiten inspeccionar/modificar requests y responses:

```kotlin
val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

client.newBuilder()
    .addInterceptor(logging)
    .build()
```

**Ventajas:**

- Loguear requests/responses
- Agregar headers automáticamente
- Manejo centralizado de errores

### ¿Cómo se definen los endpoints en la API?

**Respuesta:**
Usando interfaces en Kotlin con Retrofit:

```kotlin
interface GameApiService {
    @GET("/api/games")
    suspend fun getGames(): Response<List<Game>>

    @POST("/api/games")
    suspend fun createGame(@Body game: Game): Response<Game>

    @GET("/api/games/{id}")
    suspend fun getGameById(@Path("id") id: Int): Response<Game>
}
```

### ¿Qué es suspend en Kotlin?

**Respuesta:**
`suspend` marca una función como suspendible (puede ser pausada):

- Puede ser llamada desde una coroutine
- No bloquea el thread
- Ideal para operaciones de red

```kotlin
suspend fun getGames(): List<Game> {
    // No bloquea el thread mientras espera
    return apiService.getGames().body() ?: emptyList()
}
```

### ¿Cómo manejaste la URL de la API para diferentes entornos?

**Respuesta:**
Configuré la URL base según el entorno:

- **Emulador**: `http://10.0.2.2:4000`

  - El emulador mapea `10.0.2.2` a `localhost` del host

- **Dispositivo físico**: `http://192.168.X.X:4000`

  - Usa la IP local de la máquina

- **Producción**: `https://tu-api.vercel.app`
  - URL del servidor en la nube

Se configura en `RetrofitClient.kt`.

### ¿Cómo manejaste los errores de API?

**Respuesta:**
En los repositorios, usamos try-catch:

```kotlin
suspend fun getGames(): List<Game> {
    return try {
        val response = apiService.getGames()
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            Log.e("GameRepository", "Error: ${response.code()}")
            emptyList() // Fallback
        }
    } catch (e: Exception) {
        Log.e("GameRepository", "Exception: ${e.message}", e)
        emptyList() // Fallback
    }
}
```

---

## REPOSITORIOS Y DATOS

### ¿Qué es un Repositorio?

**Respuesta:**
El repositorio es una capa que abstrae de dónde vienen los datos:

```kotlin
class GameRepository {
    private val gameApiService = RetrofitClient.instance.create(GameApiService::class.java)

    suspend fun getGames(): List<Game> {
        // Obtener de API o base de datos local
    }
}
```

**Ventajas:**

- **Abstracción**: La UI no necesita saber si los datos vienen de API o BD local
- **Testabilidad**: Fácil mockear el repositorio
- **Mantenibilidad**: Cambios en la fuente de datos solo afectan el repositorio

### ¿Cómo funciona GameRepository?

**Respuesta:**

```kotlin
class GameRepository {
    private val gameApiService = RetrofitClient.instance.create(GameApiService::class.java)

    suspend fun getGames(): List<Game> {
        return try {
            val response = gameApiService.getGames()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            Log.e("GameRepository", "Error: ${e.message}")
            emptyList()
        }
    }
}
```

Métodos principales:

- `getGames()` - Obtiene todos los juegos
- `getGameById(id)` - Obtiene un juego específico
- `createGame(game)` - Crea un nuevo juego

### ¿Cómo funciona UserRepository?

**Respuesta:**

```kotlin
class UserRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val userApiService = RetrofitClient.instance.create(UserApiService::class.java)

    suspend fun register(user: User): Boolean {
        return try {
            val response = userApiService.registerUser(user)
            if (response.isSuccessful) {
                val registeredUser = response.body()
                saveUserData(registeredUser)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }
}
```

Gestiona:

- Registro de usuarios
- Login
- Guardado de sesión en SharedPreferences
- Foto de perfil

### ¿Qué es SharedPreferences?

**Respuesta:**
SharedPreferences es el almacenamiento local simple de Android:

```kotlin
val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

// Guardar
sharedPreferences.edit().putString("email", "user@example.com").apply()

// Obtener
val email = sharedPreferences.getString("email", null)

// Borrar
sharedPreferences.edit().clear().apply()
```

**Ventajas:**

- Simple para datos pequeños
- Automáticamente sincronizado
- Tipo seguro (strings, ints, booleans, floats, etc.)

En nuestro proyecto:

- Guardar email del usuario logueado
- Guardar URL de foto de perfil
- Guardar estado de autenticación

### ¿Cómo funciona ShoppingCartRepository?

**Respuesta:**

```kotlin
class ShoppingCartRepository {
    private val cartItems = mutableStateListOf<CartItem>()
    var lastActionMessage by mutableStateOf<String?>(null)

    fun addToCart(game: Game) {
        val existingItem = cartItems.find { it.game.id == game.id }
        if (existingItem != null) {
            // Aumentar cantidad
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = CartItem(game, existingItem.quantity + 1)
        } else {
            cartItems.add(CartItem(game, 1))
        }
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.game.price * it.quantity }
    }
}
```

Métodos:

- `addToCart()` - Agregar juego al carrito
- `removeFromCart()` - Remover juego
- `decreaseQuantity()` - Disminuir cantidad
- `getTotalPrice()` - Obtener total
- `clearCart()` - Vaciar carrito

### ¿Qué es CartItem?

**Respuesta:**

```kotlin
data class CartItem(
    val game: Game,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)
```

Representa un producto en el carrito con:

- El juego
- La cantidad
- Timestamp de cuando fue agregado

---

## TESTING

### ¿Qué es un test unitario?

**Respuesta:**
Un test unitario prueba una unidad de código en aislamiento:

- Prueba una función/clase específica
- No depende de bases de datos o APIs reales
- Se ejecuta rápidamente
- Usa mocks para dependencias externas

### ¿Qué es Kotest?

**Respuesta:**
Kotest es un framework de testing para Kotlin:

```kotlin
class GameRepositoryTest : BehaviorSpec({
    Given("Un GameRepository con API service mockeado") {
        When("Se llama a getGames()") {
            Then("Debería retornar la lista de juegos") {
                // Assertions aquí
            }
        }
    }
})
```

**Ventajas:**

- Sintaxis BDD clara
- Assertions fluidas
- Buena integración con MockK

### ¿Qué es MockK?

**Respuesta:**
MockK es una librería para crear mocks en Kotlin:

```kotlin
val mockApiService = mockk<GameApiService>()

// Mock respuesta exitosa
coEvery { mockApiService.getGames() } returns Response.success(listOf(mockGame))

// Verificar que fue llamado
coVerify { mockApiService.getGames() }
```

Permite:

- Mockear interfaces y clases
- Especificar comportamiento
- Verificar que fue llamado
- Simular excepciones

### ¿Cómo testeaste GameRepository?

**Respuesta:**

```kotlin
class GameRepositoryTest : BehaviorSpec({
    Given("Un GameRepository con API service mockeado") {
        val mockApiService = mockk<GameApiService>()
        val repository = GameRepository(mockApiService)

        When("Se llama a getGames() y API retorna éxito") {
            coEvery { mockApiService.getGames() } returns
                Response.success(listOf(mockGame1, mockGame2))

            Then("Debería retornar la lista") {
                val result = repository.getGames()
                result.shouldHaveSize(2)
                result[0].title.shouldBe("Elden Ring")
            }
        }
    }
})
```

### ¿Qué es el JUnit 5?

**Respuesta:**
JUnit 5 es el framework estándar para testing en Java/Kotlin:

- Nuevas anotaciones: `@Test`, `@BeforeEach`, `@AfterEach`
- Mejor integración con Gradle
- Soporte para parámetros en tests
- Extensiones personalizadas

```kotlin
@Test
fun testSomething() {
    // Test code
    assertTrue(condition)
}
```

### ¿Cómo funcionan los tests de coroutines?

**Respuesta:**
Usamos `runTest` para testing asincrónico:

```kotlin
@Test
fun testGetGamesReturnsCorrectData() = runTest {
    val mockApiService = mockk<GameApiService>()
    coEvery { mockApiService.getGames() } returns Response.success(listOf(mockGame))

    val result = repository.getGames() // Suspend function
    result.shouldHaveSize(1)
}
```

`runTest`:

- Proporciona un CoroutineScope
- Sincroniza el tiempo virtual
- Permite testing de funciones suspend

### ¿Qué es la cobertura de código?

**Respuesta:**
Cobertura de código es el porcentaje de código ejecutado por tests:

```
Cobertura = (Líneas ejecutadas / Líneas totales) * 100
```

Objetivo: > 80%

Para generar reporte:

```bash
./gradlew test jacoco
```

Tipos de cobertura:

- **Line coverage**: Líneas ejecutadas
- **Branch coverage**: Ramas de if/else
- **Method coverage**: Métodos ejecutados

---

## GRADLE Y BUILD

### ¿Qué es Gradle?

**Respuesta:**
Gradle es un sistema de build automatizado para Android:

- Define cómo compilar el código
- Maneja dependencias
- Define tareas de build
- Crea el APK final

### ¿Qué es build.gradle?

**Respuesta:**
`build.gradle` es el archivo de configuración del proyecto:

```gradle
android {
    namespace "com.example.kotlinapp"
    compileSdk 34

    defaultConfig {
        applicationId "com.example.kotlinapp"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }
}

dependencies {
    implementation 'androidx.compose.ui:ui:1.6.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
}
```

Define:

- Versiones de SDK
- Dependencias
- Build types (debug, release)
- Configuración de firma

### ¿Qué es gradle-wrapper (gradlew)?

**Respuesta:**
Gradle wrapper es un script que descarga y ejecuta la versión correcta de Gradle:

```bash
./gradlew build  # Usar wrapper (recomendado)
gradle build     # Usar Gradle global (menos recomendado)
```

**Ventajas:**

- Versión consistente entre desarrolladores
- No requiere instalación manual de Gradle
- Útil en CI/CD

### ¿Cuáles son los build types principales?

**Respuesta:**

**Debug:**

- Más lento
- Incluye símbolos de debug
- Fácil de debuggear
- No firmado

**Release:**

- Optimizado y rápido
- Minificado (código más pequeño)
- Firmado con certificado
- Listo para publicar

En `build.gradle`:

```gradle
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
        signingConfig signingConfigs.release
    }
}
```

### ¿Qué es ProGuard?

**Respuesta:**
ProGuard es una herramienta que:

- **Minifica**: Reduce el tamaño del código
- **Ofusca**: Renombra variables para dificultar reverse engineering
- **Optimiza**: Remueve código muerto

Archivo de configuración: `proguard-rules.pro`

```gradle
-keep public class com.example.kotlinapp.** { *; }
-keepclassmembers class ** {
    *** **(android.app.Activity);
}
```

### ¿Cómo compilaste el APK?

**Respuesta:**

```bash
./gradlew assembleRelease --no-daemon
```

Pasos:

1. `clean` - Limpia el directorio de build
2. `assembleRelease` - Compila el APK release
3. `--no-daemon` - No usar el daemon de Gradle (para evitar problemas de memoria)

Resultado: `app/build/outputs/apk/release/app-release.apk`

---

## FIRMA DE APK

### ¿Qué es un keystore?

**Respuesta:**
Un keystore es un archivo que contiene la clave privada de firma:

```bash
keytool -genkey -v -keystore release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias hakey_release -storepass Hakey@2025
```

Características:

- **Tipo**: RSA
- **Tamaño**: 2048 bits
- **Validez**: 10,000 días (~27 años)
- **Alias**: hakey_release
- **Contraseña**: Hakey@2025
- **Ubicación**: Viña del Mar, Valparaíso, Chile

### ¿Por qué es importante firmar el APK?

**Respuesta:**

- **Autenticación**: Verifica que eres realmente quien firma
- **Integridad**: Detecta si el APK fue modificado
- **Google Play**: Requiere firma con el mismo certificado siempre
- **Seguridad**: Dificulta el spoofing de apps

### ¿Cómo se configura la firma en Gradle?

**Respuesta:**
En `build.gradle`:

```gradle
signingConfigs {
    release {
        storeFile file("../release.keystore")
        storePassword "Hakey@2025"
        keyAlias "hakey_release"
        keyPassword "Hakey@2025"
    }
}

buildTypes {
    release {
        signingConfig signingConfigs.release
    }
}
```

### ¿Cuál es la diferencia entre APK sin firmar y firmado?

**Respuesta:**

| APK sin firmar                  | APK firmado             |
| ------------------------------- | ----------------------- |
| No se puede instalar en Android | Se puede instalar       |
| Debug only                      | Listo para publicar     |
| Sin verificación de autor       | Verificado digitalmente |
| Riesgo de seguridad             | Seguro                  |

---

## SEGURIDAD

### ¿Cómo protegiste los datos sensibles?

**Respuesta:**
Implementé varias capas de seguridad:

1. **Almacenamiento local**: SharedPreferences (aunque no es encriptado por defecto)
2. **HTTPS**: En producción, usar siempre HTTPS
3. **No guardar contraseñas**: Solo email en SharedPreferences
4. **Validación en cliente**: Validar antes de enviar a API

**TODO Mejorar:**

- Usar EncryptedSharedPreferences
- Implementar JWT tokens
- Encriptar contraseñas con bcrypt en backend

### ¿Cómo manejas el cleartext HTTP?

**Respuesta:**
En desarrollo, permitimos HTTP sin cifrar:

```xml
android:usesCleartextTraffic="true"
```

Pero en `network_security_config.xml`:

```xml
<domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">10.0.2.2</domain>
</domain-config>
```

**Importante**: En producción, desactivar esto y usar HTTPS.

### ¿Qué permisos requiere la app?

**Respuesta:**
En `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

**Propósitos:**

- INTERNET: Conectarse a la API
- CAMERA: Tomar fotos de perfil
- READ/WRITE STORAGE: Acceder a galería

---

## FORMULARIO DE PAGO

### ¿Cómo implementaste el formulario de pago?

**Respuesta:**
Creé `PaymentFormScreen` como un composable separado:

```kotlin
@Composable
fun PaymentFormScreen(
    totalPrice: Double,
    onPaymentSuccess: () -> Unit,
    onCancel: () -> Unit
)
```

Campos:

- Nombre del titular
- Número de tarjeta (formateado XXXX XXXX XXXX XXXX)
- Fecha de expiración (MM/YY)
- CVV (3 dígitos)
- Email

### ¿Qué validaciones implementaste?

**Respuesta:**

```kotlin
when {
    cardholderName.isEmpty() ->
        errorMessage = "Ingresa el nombre del titular"
    cardNumber.replace(" ", "").length != 16 ->
        errorMessage = "Número de tarjeta debe tener 16 dígitos"
    expiryDate.length != 5 ->
        errorMessage = "Expiración debe ser MM/YY"
    cvv.length != 3 ->
        errorMessage = "CVV debe tener 3 dígitos"
    email.isEmpty() || !email.contains("@") ->
        errorMessage = "Ingresa un email válido"
}
```

### ¿Cómo simulas el procesamiento de pago?

**Respuesta:**

```kotlin
LaunchedEffect(isProcessing) {
    if (isProcessing) {
        delay(2000) // Esperar 2 segundos
        onPaymentSuccess()
    }
}
```

Muestra un `CircularProgressIndicator` mientras procesa.

---

## PANTALLAS Y FUNCIONALIDADES

### ¿Cómo funciona la pantalla de login?

**Respuesta:**
`LoginScreen` permite al usuario autenticarse:

1. Ingresa email y contraseña
2. Valida que ambos campos no estén vacíos
3. Llama a `userRepository.login(email, password)`
4. Si es exitoso, navega a HomeActivity
5. Si falla, muestra error

### ¿Cómo funciona el registro?

**Respuesta:**
`RegisterScreen`:

1. Ingresa email y contraseña (dos veces para confirmar)
2. Valida:
   - Email válido
   - Contraseña >= 6 caracteres
   - Contraseñas coincidan
3. Llama a `userRepository.register(user)`
4. Si es exitoso, navega a login

### ¿Cómo funciona la pantalla de catálogo?

**Respuesta:**
`CatalogScreen`:

```kotlin
LazyColumn {
    items(games) { game ->
        GameCard(game, onAddToCart = { shoppingCart.addToCart(game) })
    }
}
```

1. Obtiene lista de juegos de `GameRepository`
2. Muestra cada juego en una tarjeta
3. Al hacer click en "Agregar al carrito", se agrega

### ¿Cómo funciona la pantalla de perfil?

**Respuesta:**
`AccountScreen`:

- Muestra email del usuario
- Permite cambiar nombre de perfil
- Permite seleccionar foto de:
  - Cámara
  - Galería
- Botón de logout

### ¿Cómo funciona el carrito?

**Respuesta:**
`CartScreen`:

1. Muestra lista de items en el carrito
2. Para cada item:
   - Foto del juego
   - Nombre
   - Precio individual
   - Cantidad
   - Botones: +, -, Eliminar
3. Total del carrito
4. Botón "Procesar Compra" → Abre formulario de pago

---

## PREGUNTAS TECNICAS AVANZADAS

### ¿Cuál es la diferencia entre suspend y async?

**Respuesta:**

- **suspend**: Pausa la ejecución hasta que termina
- **async**: Retorna un `Job` que se puede await() luego

```kotlin
// suspend
suspend fun fetchUser(): User = withContext(Dispatchers.IO) {
    userApi.getUser()
}

// async
fun fetchUserAsync(): Job = CoroutineScope(Dispatchers.Main).async {
    userApi.getUser()
}
```

### ¿Cómo manejas los lifecycle de Android en Compose?

**Respuesta:**
Compose abstrae el lifecycle:

```kotlin
LaunchedEffect(Unit) {
    // Se ejecuta cuando el composable entra al scope
}

DisposableEffect(Unit) {
    onDispose {
        // Se ejecuta cuando el composable sale del scope
    }
}
```

### ¿Qué es un Composable stateless?

**Respuesta:**
Un composable sin estado interno (state hoisting):

```kotlin
@Composable
fun Counter(count: Int, onCountChange: (Int) -> Unit) {
    // No tiene mutableStateOf
    Button(onClick = { onCountChange(count + 1) }) {
        Text(count.toString())
    }
}
```

**Ventajas:**

- Reutilizable
- Testeable
- Predecible

### ¿Cómo optimizaste el rendimiento?

**Respuesta:**

- Usé `LazyColumn` en lugar de `Column` para listas largas
- Usé `remember` para cachear valores
- Evité recomposiciones innecesarias con state hoisting
- Usé coroutines para operaciones de red (no bloqueo de UI)

### ¿Qué es el content scale en AsyncImage?

**Respuesta:**
Define cómo escalar la imagen:

- `ContentScale.Crop`: Recorta para llenar el espacio
- `ContentScale.Fit`: Ajusta sin recortar
- `ContentScale.Fill`: Llena el espacio (puede distorsionar)

```kotlin
AsyncImage(
    model = imageUrl,
    contentDescription = "Foto",
    contentScale = ContentScale.Crop
)
```

---

## PREGUNTAS SOBRE MEJORAS FUTURAS

### ¿Qué mejoras implementarías en el futuro?

**Respuesta:**

1. **Autenticación JWT**

   - Tokens de sesión
   - Refresh tokens automáticos
   - Logout seguro

2. **Base de datos local**

   - Room para datos en BD
   - Sincronización con API

3. **Encriptación**

   - EncryptedSharedPreferences
   - Encriptación de datos sensibles

4. **Temas**

   - Dark mode
   - Tema personalizable

5. **Notificaciones**

   - Push notifications
   - Órdenes en tiempo real

6. **Carrito persistente**

   - Guardar carrito en BD
   - Recuperar sesión anterior

7. **Análitica**
   - Tracking de eventos
   - Analytics de usuario

### ¿Cómo escalarías la app para millones de usuarios?

**Respuesta:**

1. **Backend**

   - Usar base de datos escalable (PostgreSQL)
   - Caché (Redis)
   - CDN para imágenes

2. **App**

   - Infinite scrolling en lugar de pagination
   - Caché local agresivo
   - Compresión de imágenes

3. **Infrastructure**
   - Load balancing
   - Microservicios
   - Kubernetes

---

## CONCLUSIÓN

### Resumen de conceptos clave

**Arquitectura:**

- MVVM con capas bien definidas
- Separación de responsabilidades
- Inyección de dependencias

**UI:**

- Jetpack Compose declarativo
- State hoisting
- Composables reutilizables

**Datos:**

- Retrofit para APIs
- SharedPreferences para storage local
- Repositorios para abstracción

**Testing:**

- Tests unitarios con Kotest
- Mocking con MockK
- Cobertura de código

**Build:**

- Gradle para automatización
- APK firmado para producción
- ProGuard para minificación

### Posibles preguntas de defensa

1. ¿Por qué elegiste Compose en lugar de XML?
   → Código más conciso, estado reactivo automático

2. ¿Cómo manejarías offline mode?
   → Base de datos local con Room, sincronización cuando hay conexión

3. ¿Cómo asegurarías que la app no se cuelga?
   → Usar coroutines, no operaciones bloqueantes en UI thread

4. ¿Cómo debuggearías un problema de red?
   → HttpLoggingInterceptor en Retrofit, Logcat en Android Studio

5. ¿Cómo colaborarías con un backend developer?
   → Acuerdo en contratos de API (Swagger/OpenAPI)

6. ¿Cómo manejarías fallos de API?
   → Retry logic, fallback a datos locales, mostrar error al usuario

7. ¿Por qué usaste mutableStateListOf en el carrito?
   → Para que Compose reaccione a cambios en la lista automáticamente

---

**Última actualización:** Noviembre 26, 2025
**Proyecto:** HAKEY - Android Catálogo de Juegos
**Tecnologías:** Kotlin, Jetpack Compose, Retrofit, Coroutines, Material Design 3
