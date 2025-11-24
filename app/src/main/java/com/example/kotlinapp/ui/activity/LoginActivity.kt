package com.example.kotlinapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.repository.UserRepository
import com.example.kotlinapp.ui.screens.LoginScreen
import com.example.kotlinapp.ui.theme.KotlinAppTheme

class LoginActivity : ComponentActivity() {
    
    private lateinit var userRepository: UserRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        android.util.Log.d("LoginActivity", "=== INICIO LoginActivity ===")
        
        try {
            userRepository = UserRepository(this)
            android.util.Log.d("LoginActivity", "UserRepository creado")
            
            // Si ya está logueado, ir al home
            if (userRepository.isLoggedIn()) {
                android.util.Log.d("LoginActivity", "Usuario ya logueado")
                navigateToHome()
                return
            }
            
            android.util.Log.d("LoginActivity", "Mostrando LoginScreen")
            setContent {
                KotlinAppTheme {
                    LoginScreen(
                        userRepository = userRepository,
                        onNavigateToRegister = { navigateToRegister() },
                        onNavigateToHome = { navigateToHome() }
                    )
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("LoginActivity", "ERROR: ${e.message}", e)
            
            // Mostrar error simple
            setContent {
                androidx.compose.material3.Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.Text(
                            text = "ERROR",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                            color = Color.Red
                        )
                        androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
                        androidx.compose.material3.Text(
                            text = e.message ?: "Error desconocido",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
    
    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
    
    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
