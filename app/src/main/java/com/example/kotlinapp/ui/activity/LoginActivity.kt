package com.example.kotlinapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.kotlinapp.data.repository.UserRepository
import com.example.kotlinapp.ui.screens.LoginScreen
import com.example.kotlinapp.ui.theme.KotlinAppTheme

class LoginActivity : ComponentActivity() {
    
    private lateinit var userRepository: UserRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        userRepository = UserRepository(this)
        
        // Si ya está logueado, ir al home
        if (userRepository.isLoggedIn()) {
            navigateToHome()
            return
        }
        
        setContent {
            KotlinAppTheme {
                LoginScreen(
                    userRepository = userRepository,
                    onNavigateToRegister = { navigateToRegister() },
                    onNavigateToHome = { navigateToHome() }
                )
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
