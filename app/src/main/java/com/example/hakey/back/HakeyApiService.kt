package com.example.hakey.back

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

interface HakeyApiService {

    // --- Endpoints de Juegos ---

    @GET("api/games")
    suspend fun getAllGames(): List<com.google.android.gms.games.Game>

    @GET("api/games/{id}")
    suspend fun getGameById(@Path("id") gameId: Int): com.google.android.gms.games.Game

    // --- Endpoints de Usuarios ---

    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/usuarios")
    suspend fun getAllUsers(): List<com.google.firebase.firestore.auth.User>

    @GET("api/usuarios/{id}")
    suspend fun getUserById(@Path("id") userId: Int): com.google.firebase.firestore.auth.User

    // Aquí podrías agregar el resto de endpoints (POST, PUT, DELETE) si los necesitas
}