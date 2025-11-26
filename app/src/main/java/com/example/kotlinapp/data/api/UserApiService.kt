package com.example.kotlinapp.data.api

import com.example.kotlinapp.data.model.LoginRequest
import com.example.kotlinapp.data.model.LoginResponse
import com.example.kotlinapp.data.model.RegisterRequest
import com.example.kotlinapp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    
    @GET("/api/usuarios")
    suspend fun getUsers(): Response<List<User>>
    
    @GET("/api/usuarios/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>
    
    @POST("/api/usuarios")
    suspend fun createUser(@Body user: RegisterRequest): Response<User>
    
    @POST("/api/usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    
    @PUT("/api/usuarios/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>
    
    @PATCH("/api/usuarios/{id}")
    suspend fun patchUser(@Path("id") id: Int, @Body updates: Map<String, Any>): Response<User>
    
    @DELETE("/api/usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
}
