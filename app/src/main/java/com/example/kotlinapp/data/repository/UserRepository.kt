package com.example.kotlinapp.data.repository

import android.content.Context
import android.content.SharedPreferences

class UserRepository(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PASSWORD = "user_password"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    // Registrar usuario
    fun registerUser(email: String, password: String): Boolean {
        return try {
            val trimmedEmail = email.trim().lowercase()
            val trimmedPassword = password.trim()
            sharedPreferences.edit().apply {
                putString(KEY_USER_EMAIL, trimmedEmail)
                putString(KEY_USER_PASSWORD, trimmedPassword)
                putBoolean(KEY_IS_LOGGED_IN, false)
                apply()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Login de usuario
    fun loginUser(email: String, password: String): Boolean {
        val trimmedEmail = email.trim().lowercase()
        val trimmedPassword = password.trim()
        val savedEmail = sharedPreferences.getString(KEY_USER_EMAIL, "")
        val savedPassword = sharedPreferences.getString(KEY_USER_PASSWORD, "")
        
        return if (trimmedEmail == savedEmail && trimmedPassword == savedPassword) {
            sharedPreferences.edit().apply {
                putBoolean(KEY_IS_LOGGED_IN, true)
                apply()
            }
            true
        } else {
            false
        }
    }
    
    // Verificar si está logueado
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    // Obtener email del usuario
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
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
