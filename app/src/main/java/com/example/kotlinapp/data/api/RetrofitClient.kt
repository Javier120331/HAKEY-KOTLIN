package com.example.kotlinapp.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    // URL base de la API - CAMBIA ESTO según tu entorno
    // Para desarrollo local: "http://10.0.2.2:4000" (emulador Android)
    // Para dispositivo físico en red local: "http://192.168.X.X:4000" (IP de tu PC)
    // Para producción: "https://<tu-proyecto>.vercel.app"
    private const val BASE_URL = "https://hakey-api-catalogo.vercel.app/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    val gameApiService: GameApiService by lazy {
        try {
            retrofit.create(GameApiService::class.java)
        } catch (e: Exception) {
            android.util.Log.e("RetrofitClient", "Error creando GameApiService: ${e.message}", e)
            throw e
        }
    }
    
    val userApiService: UserApiService by lazy {
        try {
            retrofit.create(UserApiService::class.java)
        } catch (e: Exception) {
            android.util.Log.e("RetrofitClient", "Error creando UserApiService: ${e.message}", e)
            throw e
        }
    }
}
