// The package name remains the same
package com.example.hakey.ui.login.ui

// Import statements are corrected and cleaned up
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Login()
    }
}


@Composable
fun Login(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        EmailField()
    }
}

@Preview(showBackground = true)
@Composable
fun EmailField() {
    TextField(
        value = "",
        onValueChange = { },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1)
}

// This commented-out section shows the correct way to reference a drawable resource.
/*
@Composable
fun ImagenHeader() {
    Image(
        // ERROR 3 FIX: You would use your app's R file, like R.drawable.logo_name
        painter = painterResource(id = com.example.hakey.R.drawable.logo_hakey_header),
        contentDescription = "Logo Hakey Header"
    )
}
*/