# HAKEY - Aplicación Android de Catálogo de Juegos

## Conexión con la API

Esta aplicación se conecta a una API de backend para obtener el catálogo de juegos y gestionar usuarios.

### Configuración de la URL de la API

La URL base de la API está configurada en el archivo:
```
app/src/main/java/com/example/kotlinapp/data/api/RetrofitClient.kt
```

#### Opciones de configuración:

1. **Para emulador Android (desarrollo local):**
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2:4000"
   ```
   El emulador de Android usa `10.0.2.2` para referirse a `localhost` de tu PC.

2. **Para dispositivo físico en red local:**
   ```kotlin
   private const val BASE_URL = "http://192.168.X.X:4000"
   ```
   Reemplaza `192.168.X.X` con la dirección IP de tu PC en la red local.
   
   Para encontrar tu IP en Windows:
   - Abre PowerShell
   - Ejecuta: `ipconfig`
   - Busca "Dirección IPv4" en tu adaptador de red activo

3. **Para producción (Vercel u otro hosting):**
   ```kotlin
   private const val BASE_URL = "https://<tu-proyecto>.vercel.app"
   ```
   Reemplaza con la URL real de tu API en producción.

### Permisos necesarios

El archivo `AndroidManifest.xml` ya incluye:
- Permiso de Internet: `<uses-permission android:name="android.permission.INTERNET" />`
- Tráfico HTTP no cifrado: `android:usesCleartextTraffic="true"` (solo para desarrollo local)

⚠️ **Importante para producción:** Cuando uses HTTPS en producción, puedes remover `android:usesCleartextTraffic="true"` del manifest.

### Endpoints disponibles

La aplicación consume los siguientes endpoints:

#### Juegos:
- `GET /api/games` - Lista todos los juegos
- `GET /api/games/:id` - Obtiene un juego por ID
- `POST /games` - Crea un juego (nota: sin prefijo /api)
- `PUT /api/games/:id` - Actualiza un juego
- `PATCH /api/games/:id` - Actualización parcial
- `DELETE /api/games/:id` - Elimina un juego

#### Usuarios:
- `GET /api/usuarios` - Lista usuarios
- `GET /api/usuarios/:id` - Obtiene un usuario por ID
- `POST /api/usuarios` - Registra un nuevo usuario
- `POST /api/usuarios/login` - Login de usuario
- `PUT /api/usuarios/:id` - Actualiza un usuario
- `PATCH /api/usuarios/:id` - Actualización parcial
- `DELETE /api/usuarios/:id` - Elimina un usuario

### Dependencias agregadas

Se han agregado las siguientes dependencias para la comunicación con la API:
- Retrofit 2.9.0 (cliente HTTP)
- Gson 2.10.1 (parsing JSON)
- OkHttp 4.11.0 (manejo de conexiones)
- Logging Interceptor (logs de red para debugging)

### Modelos de datos

#### Game (Juego)
Los juegos ahora incluyen todos los campos de la API:
- title, price, originalPrice, discount
- image, category, platform
- rating, description, requirements
- features, releaseDate, publisher, featured

#### User (Usuario)
Los usuarios incluyen:
- id, nombre, email, password, numero (opcional)

### Repositorios actualizados

#### GameRepository
- Ahora usa llamadas suspendidas (suspend functions) con coroutines
- Maneja errores de red gracefully
- Retorna listas vacías o null en caso de error

#### UserRepository
- Login y registro ahora usan la API
- Guarda la sesión localmente en SharedPreferences
- Funciones suspendidas para operaciones de red

### Cómo probar

1. **Inicia tu servidor API local:**
   ```powershell
   cd "c:\Users\soporte\Documents\COSAS JAVIER\hakey-api-catalogo-1"
   npm start
   ```

2. **Configura la URL en RetrofitClient.kt según tu entorno**

3. **Sincroniza el proyecto en Android Studio:**
   - Archivo → Sync Project with Gradle Files

4. **Ejecuta la aplicación**

### Debugging

Si tienes problemas de conexión:

1. Verifica que el servidor API esté corriendo
2. Revisa los logs en Logcat (Android Studio)
3. Busca mensajes de OkHttp con los detalles de las peticiones
4. Asegúrate de que la URL base sea correcta para tu entorno
5. En dispositivo físico, asegúrate de estar en la misma red que el servidor

### Seguridad

⚠️ **Nota importante:** La API actual no usa autenticación con tokens (JWT) y las contraseñas se manejan en texto plano. Esto es inseguro para producción. Se recomienda implementar:
- Autenticación JWT
- Hash de contraseñas (bcrypt)
- HTTPS obligatorio
- Tokens de sesión seguros
