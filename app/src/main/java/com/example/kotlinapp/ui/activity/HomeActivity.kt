package com.example.kotlinapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.repository.UserRepository
import com.example.kotlinapp.ui.screens.MainHomeScreen
import com.example.kotlinapp.ui.theme.KotlinAppTheme

class HomeActivity : ComponentActivity() {
    
    private lateinit var userRepository: UserRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        android.util.Log.d("HomeActivity", "=== INICIO HomeActivity ===")
        
        try {
            userRepository = UserRepository(this)
            android.util.Log.d("HomeActivity", "UserRepository creado")
            
            // Verificar si está logueado
            if (!userRepository.isLoggedIn()) {
                android.util.Log.d("HomeActivity", "No logueado, volviendo a Login")
                navigateToLogin()
                return
            }
            
            android.util.Log.d("HomeActivity", "Mostrando MainHomeScreen")
            setContent {
                KotlinAppTheme {
                    MainHomeScreen(
                        userRepository = userRepository,
                        onNavigateToLogin = { navigateToLogin() }
                    )
                }
            }
            android.util.Log.d("HomeActivity", "=== FIN HomeActivity onCreate ===")
        } catch (e: Exception) {
            android.util.Log.e("HomeActivity", "ERROR: ${e.message}", e)
        }
    }
    
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
