# SETUP.md - Configuración de la API

Guía completa para configurar la conexión de la aplicación con el backend API.

## 🔗 Configuración de URL Base

La URL base de la API está configurada en:

```
app/src/main/java/com/example/kotlinapp/data/api/RetrofitClient.kt
```

### Opciones según el Entorno

#### 1. **Emulador Android (Desarrollo Local)**

```kotlin
private const val BASE_URL = "http://10.0.2.2:4000"
```

- El emulador usa `10.0.2.2` para referirse a `localhost` de tu PC
- Ideal para desarrollo rápido
- No requiere IP dinámica

#### 2. **Dispositivo Físico en Red Local**

```kotlin
private const val BASE_URL = "http://192.168.X.X:4000"
```

- Reemplaza `192.168.X.X` con la dirección IP de tu PC
- El teléfono y PC deben estar en la misma red WiFi

**Encontrar tu IP en Windows:**

```powershell
ipconfig
# Busca "Dirección IPv4" en tu adaptador de red activo
# Ej: 192.168.1.105:4000
```

#### 3. **Producción (Vercel/Hosting)**

```kotlin
private const val BASE_URL = "https://tu-api.vercel.app"
```

- Reemplaza con la URL real de tu API
- Usar siempre HTTPS en producción

## 📱 Permisos Necesarios

El archivo `AndroidManifest.xml` incluye automáticamente:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<application ... android:usesCleartextTraffic="true" ... >
```

### Notas de Seguridad

- `android:usesCleartextTraffic="true"` permite HTTP sin cifrar (solo para desarrollo)
- ⚠️ En producción con HTTPS, puedes remover esta línea
- En Android 9+, se recomienda usar network security configuration

## 🔌 Endpoints Disponibles

### Juegos

```
GET  /api/games              - Lista todos los juegos
GET  /api/games/:id          - Obtiene un juego por ID
POST /games                  - Crea un juego (nota: sin prefijo /api)
PUT  /api/games/:id          - Actualiza un juego
PATCH /api/games/:id         - Actualización parcial de juego
DELETE /api/games/:id        - Elimina un juego
```

### Usuarios

```
GET    /api/usuarios         - Lista todos los usuarios
GET    /api/usuarios/:id     - Obtiene un usuario por ID
POST   /api/usuarios         - Registra un nuevo usuario
POST   /api/usuarios/login   - Login de usuario
PUT    /api/usuarios/:id     - Actualiza un usuario
PATCH  /api/usuarios/:id     - Actualización parcial
DELETE /api/usuarios/:id     - Elimina un usuario
```

## 📦 Modelos de Datos

### Game (Juego)

```kotlin
data class Game(
    val id: String,
    val title: String,
    val price: Double,
    val originalPrice: Double,
    val discount: Int,
    val image: String,
    val category: String,
    val platform: String,
    val rating: Double,
    val description: String,
    val requirements: String,
    val features: List<String>,
    val releaseDate: String,
    val publisher: String,
    val featured: Boolean
)
```

### User (Usuario)

```kotlin
data class User(
    val id: String?,
    val nombre: String,
    val email: String,
    val password: String,
    val numero: String? = null
)
```

### CartItem (Artículo del Carrito)

```kotlin
data class CartItem(
    val game: Game,
    val quantity: Int,
    val addedAt: Long
)
```

## 🧰 Dependencias de Red

Las siguientes librerías han sido agregadas a `app/build.gradle`:

```gradle
// Retrofit - Cliente HTTP
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// OkHttp - Manejo de conexiones
implementation 'com.squareup.okhttp3:okhttp:4.11.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'

// Gson - Parsing JSON
implementation 'com.google.code.gson:gson:2.10.1'

// Coroutines - Asincronía
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
```

## 🔍 Debugging de Conexión

Si tienes problemas de conexión:

1. **Verificar que el servidor esté corriendo:**

   ```powershell
   # En Windows PowerShell
   netstat -ano | findstr :4000
   # Si hay resultado, el servidor está corriendo
   ```

2. **Revisar logs en Logcat (Android Studio):**

   - View → Tool Windows → Logcat
   - Busca mensajes de "OkHttp" o "Retrofit"
   - Filtra por tu package: `com.example.kotlinapp`

3. **Verificar configuración:**

   - ✅ URL base es correcta
   - ✅ Permisos de INTERNET en AndroidManifest.xml
   - ✅ Emulador: usa `10.0.2.2` no `localhost`
   - ✅ Dispositivo físico: PC y teléfono en misma red

4. **Inspeccionar peticiones:**

   - El `LoggingInterceptor` de OkHttp registra todas las peticiones
   - Busca en Logcat logs con el contenido de request/response

5. **Probar conexión manualmente:**
   - Usa Postman o Thunder Client
   - Verifica que los endpoints respondan correctamente
   - Prueba con la misma URL que configuraste

## 🚀 Inicio del Servidor API

En otra terminal PowerShell:

```powershell
# Navega a la carpeta del API
cd "c:\Users\soporte\Documents\COSAS JAVIER\hakey-api-catalogo-1"

# Instala dependencias (primera vez)
npm install

# Inicia el servidor
npm start

# Deberías ver:
# Server running on port 4000
```

## 💾 Almacenamiento Local

### SharedPreferences

La aplicación usa SharedPreferences para:

- **Email del usuario** - Guardado en login
- **Estado de autenticación** - Flag de usuario logueado
- **Sesión local** - Persistencia entre reinicios

### Modelos locales

- `CartItem` - Ítems del carrito en memoria (no persistente actualmente)
- Considera SQLite para persistencia de carrito a largo plazo

## 🔐 Interceptores

RetrofitClient implementa automáticamente:

**LoggingInterceptor:**

- Registra todas las peticiones HTTP
- Muestra headers, URL y body
- Útil para debugging

**Ejemplo de log:**

```
--> POST http://10.0.2.2:4000/api/usuarios/login
Content-Type: application/json

{"email":"test@example.com","password":"12345"}

<-- 200 OK (125ms)
```

## ⚡ Manejo de Errores

### GameRepository

- Retorna lista vacía `[]` si falla la API
- No lanza excepciones, manejo graceful de errores
- Logged en Logcat para debugging

### UserRepository

- Retorna `null` si login falla
- Valida respuestas de API
- Almacena sesión solo si autenticación exitosa

## 🎯 Próximos Pasos

1. Configura la URL base para tu entorno
2. Inicia el servidor API
3. Ejecuta la app en emulador o dispositivo
4. Revisa Logcat si hay problemas
5. Prueba registro y login

---

**Última actualización:** Noviembre 2025
