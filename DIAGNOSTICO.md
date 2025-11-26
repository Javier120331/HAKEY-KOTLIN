# 🔍 DIAGNÓSTICO: App no abre en Android Studio

## PASO 1: Ver el error en Logcat

En Android Studio:

1. **Ejecuta la app** (botón Run ▶️)
2. **Abre Logcat** (pestaña en la parte inferior de la pantalla)
3. **Filtra por tu app:**
   - En el campo de búsqueda de Logcat, escribe: `package:com.example.kotlinapp`
   - O selecciona tu app en el dropdown de dispositivos

4. **Busca el error:**
   - Líneas en ROJO con la etiqueta `E/AndroidRuntime`
   - Busca: `FATAL EXCEPTION`
   - Busca: `Caused by:`

## PASO 2: Errores comunes y cómo identificarlos

### ❌ Error: "ClassNotFoundException" o "NoClassDefFoundError"
**Síntoma:** No encuentra clases de Retrofit/Gson
**Solución:** Gradle no sincronizado
```
File → Sync Project with Gradle Files
```

### ❌ Error: "NetworkOnMainThreadException"
**Síntoma:** Intenta hacer llamadas de red en el hilo principal
**Solución:** Ya está arreglado con coroutines, pero revisa Logcat

### ❌ Error: "ConnectException" o "SocketTimeoutException"
**Síntoma:** No puede conectar con la API
**Solución:** El servidor no está corriendo o la URL es incorrecta

### ❌ Error: "Unable to start activity"
**Síntoma:** Crash al iniciar LoginActivity
**Solución:** Problema en el onCreate() - revisar Logcat

### ❌ Error relacionado con Compose
**Síntoma:** Error al renderizar la UI
**Solución:** Problema de compatibilidad de versiones

## PASO 3: Mientras revisas Logcat, prueba esto

### Opción A: Desactivar verificación de login al inicio

Temporalmente comenta la verificación de login para que la app abra:

**Archivo:** `LoginActivity.kt`

Comenta estas líneas:
```kotlin
// Si ya está logueado, ir al home
/* COMENTAR TEMPORALMENTE
if (userRepository.isLoggedIn()) {
    navigateToHome()
    return
}
*/
```

### Opción B: Agregar try-catch para capturar errores

Agrega logging para ver dónde falla:

**En LoginActivity.kt - onCreate():**
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    try {
        android.util.Log.d("LoginActivity", "Iniciando...")
        userRepository = UserRepository(this)
        android.util.Log.d("LoginActivity", "UserRepository creado")
        
        // Si ya está logueado, ir al home
        if (userRepository.isLoggedIn()) {
            android.util.Log.d("LoginActivity", "Usuario logueado, navegando a Home")
            navigateToHome()
            return
        }
        
        android.util.Log.d("LoginActivity", "Mostrando LoginScreen")
        setContent {
            // ... resto del código
        }
    } catch (e: Exception) {
        android.util.Log.e("LoginActivity", "ERROR: ${e.message}", e)
        e.printStackTrace()
    }
}
```

## PASO 4: Verificar que Gradle sincronizó correctamente

En Android Studio:

1. Abre: **Build → Build Project** (Ctrl+F9)
2. Revisa la ventana **Build** (abajo) para ver si hay errores
3. Si hay errores de dependencias:
   - **File → Invalidate Caches → Invalidate and Restart**

## PASO 5: Revisar permisos en AndroidManifest.xml

Verifica que tenga:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Y en `<application>`:
```xml
android:usesCleartextTraffic="true"
```

## 🎯 LO MÁS IMPORTANTE

**NECESITO VER EL ERROR DE LOGCAT**

Por favor:
1. Ejecuta la app
2. Espera a que crashee
3. Ve a Logcat
4. Busca las líneas rojas con "FATAL EXCEPTION"
5. **Copia TODO el error** (desde "FATAL EXCEPTION" hasta el final del stack trace)
6. Compártelo conmigo

Sin ver el error exacto, solo puedo adivinar. El error de Logcat me dirá exactamente qué está fallando.

## 📱 Alternativa rápida: Probar con datos locales

Si quieres que la app abra YA (sin API), puedo modificar temporalmente `GameRepository` y `UserRepository` para que NO usen la API y funcionen con datos locales hardcodeados.

¿Quieres que haga eso mientras investigas el error de Logcat?
