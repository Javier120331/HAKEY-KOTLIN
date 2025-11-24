package com.example.kotlinapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.kotlinapp.data.api.RetrofitClient
import com.example.kotlinapp.data.model.LoginRequest
import com.example.kotlinapp.data.model.RegisterRequest
import com.example.kotlinapp.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    
    private val apiService by lazy {
        try {
            RetrofitClient.userApiService
        } catch (e: Exception) {
            android.util.Log.e("UserRepository", "Error inicializando API: ${e.message}")
            null
        }
    }
    
    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_DISPLAY_NAME = "display_name"
        private const val KEY_PROFILE_IMAGE_URL = "profile_image_url"
    }
    
    // Registrar usuario en la API
    suspend fun registerUser(nombre: String, email: String, password: String, numero: String? = null): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("UserRepository", "API no disponible para registro")
                    return@withContext false
                }
                
                val request = RegisterRequest(
                    nombre = nombre,
                    email = email,
                    password = password,
                    numero = numero
                )
                val response = service.createUser(request)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    // Guardar datos localmente
                    sharedPreferences.edit().apply {
                        putInt(KEY_USER_ID, user.id)
                        putString(KEY_USER_EMAIL, user.email)
                        putString(KEY_USER_NAME, user.nombre)
                        putString(KEY_DISPLAY_NAME, user.nombre)
                        putBoolean(KEY_IS_LOGGED_IN, false)
                        apply()
                    }
                    true
                } else {
                    android.util.Log.e("UserRepository", "Registro falló: ${response.code()} - ${response.message()}")
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("UserRepository", "Error en registro: ${e.message}", e)
                false
            }
        }
    }
    
    // Login de usuario con la API
    suspend fun loginUser(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("UserRepository", "API no disponible para login")
                    return@withContext false
                }
                
                val request = LoginRequest(email = email, password = password)
                val response = service.login(request)
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    val user = loginResponse.usuario
                    // Guardar sesión localmente
                    sharedPreferences.edit().apply {
                        putInt(KEY_USER_ID, user.id)
                        putString(KEY_USER_EMAIL, user.email)
                        putString(KEY_USER_NAME, user.nombre)
                        putString(KEY_DISPLAY_NAME, user.nombre)
                        putBoolean(KEY_IS_LOGGED_IN, true)
                        apply()
                    }
                    true
                } else {
                    android.util.Log.e("UserRepository", "Login falló: ${response.code()} - ${response.message()}")
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("UserRepository", "Error en login: ${e.message}", e)
                false
            }
        }
    }
    
    // Obtener todos los usuarios
    suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("UserRepository", "API no disponible para obtener usuarios")
                    return@withContext emptyList()
                }
                
                val response = service.getUsers()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    // Obtener usuario por ID
    suspend fun getUserById(id: Int): User? {
        return withContext(Dispatchers.IO) {
            try {
                val service = apiService ?: run {
                    android.util.Log.e("UserRepository", "API no disponible para obtener usuario")
                    return@withContext null
                }
                
                val response = service.getUserById(id)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    // Verificar si está logueado
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    // Obtener ID del usuario
    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 0)
    }
    
    // Obtener email del usuario
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    // Obtener nombre del usuario
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }

    // Obtener display name del usuario
    fun getDisplayName(): String? {
        return sharedPreferences.getString(KEY_DISPLAY_NAME, null)
    }

    // Guardar display name del usuario
    fun setDisplayName(name: String) {
        sharedPreferences.edit().putString(KEY_DISPLAY_NAME, name).apply()
    }

    // Obtener URL de la foto de perfil
    fun getProfileImageUrl(): String? {
        return sharedPreferences.getString(KEY_PROFILE_IMAGE_URL, null)
    }

    // Guardar URL de la foto de perfil
    fun setProfileImageUrl(url: String) {
        sharedPreferences.edit().putString(KEY_PROFILE_IMAGE_URL, url).apply()
    }
    
    // Logout
    fun logout() {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }
    
    // Limpiar todos los datos
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
