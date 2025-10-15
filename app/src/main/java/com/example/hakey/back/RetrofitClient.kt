package com.example.hakey.back

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // IMPORTANTE: Reemplaza "TU_DIRECCION_IP_LOCAL" con la IP de tu computadora.
    // Si usas el emulador, la IP especial 10.0.2.2 apunta al localhost de tu máquina.
    // Si usas un dispositivo físico, debe ser la IP de tu PC en la misma red WiFi.
    private const val BASE_URL = "http://10.0.2.2:4000/"

    val instance: HakeyApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(HakeyApiService::class.java)
    }
}