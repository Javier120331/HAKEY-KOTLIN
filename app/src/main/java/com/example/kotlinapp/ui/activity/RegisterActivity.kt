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
        
        userRepository = UserRepository(this)
        
        setContent {
            KotlinAppTheme {
                RegisterScreen(
                    userRepository = userRepository,
                    onNavigateToLogin = { finish() }
                )
            }
        }
    }
}
