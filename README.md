# HAKEY - Aplicación Android de Catálogo de Juegos

Aplicación móvil completa en Kotlin con autenticación de usuarios, catálogo de juegos integrado con API REST y carrito de compras.

## 🎮 Características Principales

✅ **Autenticación** - Registro, Login y gestión de sesiones  
✅ **Catálogo de Juegos** - Integración con API REST para obtener juegos  
✅ **Carrito de Compras** - Agregar, modificar y remover productos  
✅ **Interfaz Moderna** - Diseño con Jetpack Compose  
✅ **Suite de Pruebas** - Tests unitarios y UI completos  
✅ **Gestión de Datos** - SharedPreferences y APIs remotas

## 📁 Estructura del Proyecto

```
app/src/main/
├── java/com/example/kotlinapp/
│   ├── data/
│   │   ├── api/
│   │   │   ├── GameApiService.kt      # Endpoints de juegos
│   │   │   ├── UserApiService.kt      # Endpoints de usuarios
│   │   │   └── RetrofitClient.kt      # Configuración de Retrofit
│   │   ├── model/
│   │   │   ├── Game.kt                # Modelo de juego
│   │   │   ├── User.kt                # Modelo de usuario
│   │   │   └── CartItem.kt            # Modelo del carrito
│   │   └── repository/
│   │       ├── GameRepository.kt      # Gestión de juegos
│   │       ├── UserRepository.kt      # Gestión de usuarios
│   │       └── ShoppingCartRepository.kt # Gestión del carrito
│   │
│   └── ui/
│       ├── activity/
│       │   ├── LoginActivity.kt
│       │   ├── RegisterActivity.kt
│       │   └── HomeActivity.kt
│       ├── screens/
│       │   ├── LoginScreen.kt
│       │   ├── RegisterScreen.kt
│       │   ├── HomeScreen.kt
│       │   ├── MainHomeScreen.kt
│       │   └── OtherScreens.kt
│       ├── navigation/
│       │   └── NavigationItem.kt
│       └── theme/
│           └── Theme.kt
│
├── res/
│   ├── layout/ (XMLs de actividades)
│   ├── drawable/ (Recursos gráficos)
│   └── values/ (Colores, strings, temas)
│
└── AndroidManifest.xml

test/java/ & androidTest/java/   # Tests unitarios e UI
```

## 🚀 Guía de Inicio Rápido

### Requisitos Previos

- Android Studio Arctic Fox o superior
- JDK 11+
- Android SDK 28+
- Emulador o dispositivo físico

### Instalación

1. **Clona el repositorio:**

   ```bash
   git clone https://github.com/Javier120331/HAKEY-KOTLIN.git
   cd HAKEY-KOTLIN
   ```

2. **Sincroniza Gradle en Android Studio:**

   - Archivo → Sync Project with Gradle Files

3. **Configura la URL de la API** (ver SETUP.md)

4. **Ejecuta la aplicación:**
   - Selecciona el emulador/dispositivo
   - Click en "Run" (Shift + F10)

## 📋 Funcionalidades Detalladas

### 1. Autenticación

- Validación de email y contraseña
- Registro de nuevos usuarios
- Gestión de sesiones con SharedPreferences
- Logout y cierre de sesión

### 2. Catálogo de Juegos

- Lista completa de juegos desde API
- Filtrado por categoría
- Búsqueda por título
- Detalles de juego con descripción y requisitos

### 3. Carrito de Compras

- Agregar/remover productos
- Modificar cantidades
- Cálculo automático de total
- Persistencia local

### 4. Interfaz de Usuario

- Diseño responsive con Jetpack Compose
- Temas personalizables
- Navegación intuitiva
- Indicadores de carga

## 🧪 Testing

### Ejecutar Tests Unitarios

```bash
./gradlew test
```

### Ejecutar Tests UI

```bash
./gradlew connectedAndroidTest
```

### Cobertura de Tests

- Tests unitarios para Repositorios y APIs
- Tests de UI para pantallas principales
- Manejo de errores y casos edge
- Mock de servicios con MockK

Para más información, consulta `TESTING.md`

## 📦 Dependencias Principales

- **Jetpack Compose** - Interfaz moderna declarativa
- **Retrofit 2** - Cliente HTTP
- **Gson** - Parsing JSON
- **Coroutines** - Programación asincrónica
- **SharedPreferences** - Almacenamiento local
- **Kotest** - Framework de testing

## 🔗 Configuración de API

Para detalles sobre configuración de conexión con API, permisos, endpoints y debugging, consulta `SETUP.md`

## ⚙️ Configuración del Proyecto

- **AndroidManifest.xml** - Permisos de Internet habilitados
- **gradle.properties** - Versiones de SDK y compilación
- **ProGuard Rules** - Ofuscación de código en build release

## 🔒 Seguridad (Nota Importante)

La aplicación actual:

- ⚠️ NO usa autenticación JWT
- ⚠️ Contraseñas en texto plano

Para producción, implementar:

- Autenticación JWT
- Hash de contraseñas (bcrypt)
- HTTPS obligatorio
- Tokens de sesión seguros

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin
- **Plataforma:** Android (API 28+)
- **Build System:** Gradle
- **UI:** Jetpack Compose
- **Testing:** Kotest, MockK, JUnit 5
- **Networking:** Retrofit + OkHttp
- **JSON:** Gson

## 📚 Documentación Complementaria

- **[SETUP.md](SETUP.md)** - Configuración de API y entorno
- **[TESTING.md](TESTING.md)** - Guía completa de pruebas

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Proyecto privado - Todos los derechos reservados.

---

**Última actualización:** Noviembre 2025
