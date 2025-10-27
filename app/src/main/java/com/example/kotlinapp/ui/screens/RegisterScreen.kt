package com.example.kotlinapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.data.repository.UserRepository
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    userRepository: UserRepository,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    // Mensajes de error por campo
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    LaunchedEffect(showSuccess) {
        if (showSuccess) {
            kotlinx.coroutines.delay(1500)
            onNavigateToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro",
            fontSize = 28.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de texto de Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showError = false
                // Validación en tiempo real del email
                emailError = when {
                    it.isEmpty() -> ""
                    !it.contains("@") || !it.contains(".") -> "Email inválido"
                    else -> ""
                }
            },
            label = { Text("Email") },
            isError = emailError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .onKeyEvent { keyEvent ->
                    keyEvent.key == Key.Tab
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(8.dp)
        )
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Campo de texto de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showError = false
                // Validación en tiempo real de la contraseña
                passwordError = when {
                    it.isEmpty() -> ""
                    it.length <= 6 -> "La contraseña debe tener más de 6 caracteres"
                    else -> ""
                }
            },
            label = { Text("Contraseña") },
            isError = passwordError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .onKeyEvent { keyEvent ->
                    keyEvent.key == Key.Tab
                },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(8.dp)
        )
        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Campo de texto de Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                showError = false
                // Validación en tiempo real de confirmación
                confirmPasswordError = when {
                    it.isEmpty() -> ""
                    it != password -> "Las contraseñas no coinciden"
                    else -> ""
                }
            },
            label = { Text("Confirmar Contraseña") },
            isError = confirmPasswordError.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .onKeyEvent { keyEvent ->
                    keyEvent.key == Key.Tab
                },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(8.dp)
        )
        if (confirmPasswordError.isNotEmpty()) {
            Text(
                text = confirmPasswordError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Mensaje de error (del repositorio o validación final)
        if (showError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        // Mensaje de éxito
        if (showSuccess) {
            Text(
                text = "Registro exitoso. Ahora inicia sesión",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        // Botón de Registro (habilitado solo cuando el formulario es válido)
        val isFormValid = email.isNotBlank() && password.length > 6 && password == confirmPassword && email.contains("@") && email.contains(".")

        Button(
            onClick = {
                // Verificación de seguridad final antes del registro
                if (!isFormValid) {
                    errorMessage = "Por favor corrige los datos del formulario"
                    showError = true
                    return@Button
                }

                if (userRepository.registerUser(email, password)) {
                    showSuccess = true
                    showError = false
                    email = ""
                    password = ""
                    confirmPassword = ""
                    emailError = ""
                    passwordError = ""
                    confirmPasswordError = ""
                } else {
                    errorMessage = "Error en el registro"
                    showError = true
                }
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Registrarse",
                fontSize = 16.sp,
                color = if (isFormValid) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Enlace de Login
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tienes cuenta? ",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = "Inicia Sesión",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToLogin() },
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}
