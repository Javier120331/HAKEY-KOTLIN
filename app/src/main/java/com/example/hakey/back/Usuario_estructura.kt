package com.example.hakey.back
import com.google.gson.annotations.SerializedName

// Para recibir la info de un usuario (sin contraseña)
data class Usuario_estructura(
    val id: Int,
    val nombre: String,
    val email: String
)

// Para enviar en el cuerpo de la petición de login
data class LoginRequest(
    val email: String,
    val password: String
)

// Para recibir la respuesta del login
data class LoginResponse(
    val message: String,
    @SerializedName("usuario")
    val user: Usuario_estructura
)