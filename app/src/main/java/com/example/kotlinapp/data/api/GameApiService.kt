package com.example.kotlinapp.data.api

import com.example.kotlinapp.data.model.Game
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GameApiService {
    
    @GET("/api/games")
    suspend fun getGames(): Response<List<Game>>
    
    @GET("/api/games/{id}")
    suspend fun getGameById(@Path("id") id: Int): Response<Game>
    
    @POST("/games")
    suspend fun createGame(@Body game: Game): Response<Game>
    
    @PUT("/api/games/{id}")
    suspend fun updateGame(@Path("id") id: Int, @Body game: Game): Response<Game>
    
    @PATCH("/api/games/{id}")
    suspend fun patchGame(@Path("id") id: Int, @Body updates: Map<String, Any>): Response<Game>
    
    @DELETE("/api/games/{id}")
    suspend fun deleteGame(@Path("id") id: Int): Response<Unit>
}
