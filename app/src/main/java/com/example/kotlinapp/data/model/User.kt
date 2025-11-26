package com.example.kotlinapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("nombre")
    val nombre: String = "",
    
    @SerializedName("email")
    val email: String = "",
    
    @SerializedName("password")
    val password: String = "",
    
    @SerializedName("numero")
    val numero: String? = null
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    
    @SerializedName("usuario")
    val usuario: User
)

data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("numero")
    val numero: String? = null
)
