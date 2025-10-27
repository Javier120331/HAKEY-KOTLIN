# Kotlin Android App

Proyecto básico en Kotlin para Android con funcionalidades de Login, Register y Home.

## Características

✅ **Login** - Autenticación de usuarios
✅ **Register** - Registro de nuevos usuarios  
✅ **Local Storage** - Almacenamiento con SharedPreferences
✅ **Home** - Pantalla vacía después del login
✅ **Validaciones** - Email, contraseña y campos requeridos

## Estructura del Proyecto

```
app/src/main/
├── java/com/example/kotlinapp/
│   ├── data/
│   │   └── repository/
│   │       └── UserRepository.kt      # Gestión de datos locales
│   └── ui/
│       └── activity/
│           ├── LoginActivity.kt       # Pantalla de login
│           ├── RegisterActivity.kt    # Pantalla de registro
│           └── HomeActivity.kt        # Pantalla principal
├── res/
│   ├── layout/
│   │   ├── activity_login.xml
│   │   ├── activity_register.xml
│   │   └── activity_home.xml
│   ├── drawable/
│   │   ├── edit_text_background.xml
│   │   └── button_background.xml
│   └── values/
│       ├── colors.xml
│       ├── strings.xml
│       └── themes.xml
└── AndroidManifest.xml
```

## Funcionalidades

### 1. Registro (Register)
- Valida email válido
- Valida contraseña mínimo 6 caracteres
- Confirma que las contraseñas coincidan
- Guarda datos en SharedPreferences

### 2. Login
- Valida email y contraseña
- Verifica credenciales contra datos guardados
- Marca usuario como logueado
- Redirige al Home

### 3. Home
- Muestra mensaje de bienvenida con el email del usuario
- Botón de Logout que limpia la sesión
- Redirecciona a Login cuando se cierra sesión

### 4. Almacenamiento Local
- **SharedPreferences** para guardar:
  - Email del usuario
  - Contraseña
  - Estado de login

## Cómo Usar

1. Abre el proyecto en Android Studio
2. Sincroniza Gradle
3. Ejecuta la app en un emulador o dispositivo físico
4. Regístrate con un email y contraseña
5. Inicia sesión con tus credenciales
6. Verás la pantalla de Home

## Dependencias

- AndroidX Core
- Material Components
- ConstraintLayout
- Lifecycle

## Nota

Esta es una app básica de demostración. Para producción considera:
- Encriptar contraseñas
- Usar una base de datos SQLite
- Implementar autenticación en servidor
- Agregar validaciones más robustas
