# 🔧 Solución: La App No Abre

## Pasos para solucionar el problema:

### 1️⃣ SINCRONIZAR GRADLE (MUY IMPORTANTE)

Después de agregar las nuevas dependencias de Retrofit, **DEBES sincronizar Gradle**:

**En Android Studio:**
1. Abre el proyecto en Android Studio
2. Ve a: **File → Sync Project with Gradle Files**
3. Espera a que termine la sincronización (puede tardar varios minutos)
4. Si ves errores, hazle clic a "Try Again" o limpia el proyecto

**O desde el menú:**
- **Build → Clean Project**
- Luego: **Build → Rebuild Project**

---

### 2️⃣ VERIFICAR LA URL DE LA API

La app intenta conectarse a la API al iniciar. Si no puede conectarse, puede crashear.

**Archivo:** `app/src/main/java/com/example/kotlinapp/data/api/RetrofitClient.kt`

```kotlin
private const val BASE_URL = "http://10.0.2.2:4000"
```

**Cambia según tu caso:**

#### Para EMULADOR Android:
```kotlin
private const val BASE_URL = "http://10.0.2.2:4000"
```
✅ Ya está configurado así

#### Para DISPOSITIVO FÍSICO en la misma red:
```kotlin
private const val BASE_URL = "http://192.168.X.X:4000"
```
Reemplaza `192.168.X.X` con la IP de tu PC.

Para saber tu IP:
```powershell
ipconfig
```
Busca "Dirección IPv4"

---

### 3️⃣ VERIFICAR QUE EL SERVIDOR API ESTÉ CORRIENDO

La app **necesita** que el servidor esté corriendo:

```powershell
cd "c:\Users\soporte\Documents\COSAS JAVIER\hakey-api-catalogo-1"
npm start
```

Deberías ver algo como:
```
Server running on port 4000
```

---

### 4️⃣ VER LOS LOGS DE LA APP

**En Android Studio:**
1. Ve a la pestaña **Logcat** (abajo)
2. Filtra por tu app: `com.example.kotlinapp`
3. Busca errores en rojo que digan:
   - `ConnectException`
   - `SocketTimeoutException`
   - `UnknownHostException`
   - Cualquier línea en rojo

---

### 5️⃣ HACER UN CLEAN & REBUILD

A veces Android Studio necesita limpiar cachés:

1. **Build → Clean Project**
2. Espera que termine
3. **Build → Rebuild Project**
4. Espera que termine
5. Vuelve a ejecutar la app

---

### 6️⃣ INVALIDAR CACHÉS (Si nada funciona)

En Android Studio:
1. **File → Invalidate Caches...**
2. Selecciona **"Invalidate and Restart"**
3. Android Studio se reiniciará
4. Espera que recargue todo
5. Vuelve a sincronizar Gradle

---

### 7️⃣ DESINSTALAR LA APP DEL DISPOSITIVO

Si la app vieja está instalada, desinstálala primero:

```powershell
adb uninstall com.example.kotlinapp
```

Luego vuelve a instalar desde Android Studio.

---

## 🐛 Errores Comunes y Soluciones

### Error: "Failed to connect to /10.0.2.2:4000"
**Causa:** El servidor API no está corriendo  
**Solución:** Inicia el servidor con `npm start`

### Error: "Unable to resolve host"
**Causa:** URL incorrecta o problema de red  
**Solución:** Verifica la URL en `RetrofitClient.kt`

### Error: "Cleartext HTTP traffic not permitted"
**Causa:** Android bloquea HTTP por defecto  
**Solución:** Ya agregué `android:usesCleartextTraffic="true"` en el Manifest

### La app se cierra inmediatamente
**Causa:** Crash al iniciar, probablemente por la API  
**Solución:** Revisa Logcat para ver el error exacto

---

## ✅ CHECKLIST RÁPIDO

Marca cada paso:

- [ ] Sincronizar Gradle en Android Studio
- [ ] Servidor API corriendo en `http://localhost:4000`
- [ ] URL correcta en `RetrofitClient.kt`
- [ ] Build → Clean Project
- [ ] Build → Rebuild Project
- [ ] Desinstalar app vieja del dispositivo
- [ ] Instalar app nueva
- [ ] Revisar Logcat si falla

---

## 📱 Probar sin la API (Temporalmente)

Si quieres probar la app SIN la API, puedes volver al código anterior temporalmente:

En `GameRepository.kt` y `UserRepository.kt` puedes comentar las llamadas a la API y usar datos hardcodeados solo para probar que la app abre.

Pero primero **intenta los pasos de arriba**, el problema más probable es que Gradle no está sincronizado.

---

## 🆘 Si Nada Funciona

Compárteme el error exacto de Logcat. Copia todo el mensaje de error en rojo y te ayudo a resolverlo.
