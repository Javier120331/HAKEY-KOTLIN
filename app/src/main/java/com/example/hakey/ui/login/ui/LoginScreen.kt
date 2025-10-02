package com.example.hakey.ui.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hakey.R

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val email by viewModel.email.observeAsState(initial = "")
    val password by viewModel.password.observeAsState(initial = "")
    val isEmailError by viewModel.isEmailError.observeAsState(initial = false)
    val isPasswordError by viewModel.isPasswordError.observeAsState(initial = false)
    val isLoginButtonEnabled by viewModel.isLoginButtonEnabled.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logohy),
            contentDescription = "Logo",
            modifier = Modifier.align(Alignment.TopCenter).size(200.dp)




        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Inicio de Sesión", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onLoginDataChanged(it, password) },
            label = { Text("Correo Electrónico") },
            singleLine = true,
            isError = isEmailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        if (isEmailError) {
            Text(
                text = "Por favor, introduce un email válido.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onLoginDataChanged(email, it) },
            label = { Text("Contraseña") },
            singleLine = true,
            isError = isPasswordError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        if (isPasswordError) {
            Text(
                text = "La contraseña debe tener al menos 8 caracteres.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onLoginClicked() },
            modifier = Modifier.fillMaxWidth(),
            enabled = isLoginButtonEnabled
        ) {
            Text("Iniciar Sesión")
        }
    }
}
