package com.example.kotlinapp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kotlinapp.data.repository.UserRepository
import com.example.kotlinapp.ui.screens.RegisterScreen
import com.example.kotlinapp.ui.theme.KotlinAppTheme

class RegisterActivity : ComponentActivity() {
    
    private lateinit var userRepository: UserRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            android.util.Log.d("RegisterActivity", "Iniciando RegisterActivity...")
            
            userRepository = UserRepository(this)
            android.util.Log.d("RegisterActivity", "UserRepository inicializado")
            
            setContent {
                KotlinAppTheme {
                    RegisterScreen(
                        userRepository = userRepository,
                        onNavigateToLogin = { finish() }
                    )
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("RegisterActivity", "ERROR en onCreate: ${e.message}", e)
            e.printStackTrace()
        }
    }
}
