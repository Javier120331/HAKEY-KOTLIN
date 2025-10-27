package com.example.kotlinapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kotlinapp.data.repository.UserRepository
import com.example.kotlinapp.ui.screens.MainHomeScreen
import com.example.kotlinapp.ui.theme.KotlinAppTheme

class HomeActivity : ComponentActivity() {
    
    private lateinit var userRepository: UserRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        userRepository = UserRepository(this)
        
        // Verificar si está logueado
        if (!userRepository.isLoggedIn()) {
            navigateToLogin()
            return
        }
        
        setContent {
            KotlinAppTheme {
                MainHomeScreen(
                    userRepository = userRepository,
                    onNavigateToLogin = { navigateToLogin() }
                )
            }
        }
    }
    
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
