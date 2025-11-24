package com.example.kotlinapp.data.model

import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("title")
    val title: String = "",
    
    @SerializedName("price")
    val price: Double = 0.0,
    
    @SerializedName("originalPrice")
    val originalPrice: Double? = null,
    
    @SerializedName("discount")
    val discount: Double? = null,
    
    @SerializedName("image")
    val image: String = "",
    
    @SerializedName("category")
    val category: String = "",
    
    @SerializedName("platform")
    val platform: List<String> = emptyList(),
    
    @SerializedName("rating")
    val rating: Float = 0f,
    
    @SerializedName("description")
    val description: String = "",
    
    @SerializedName("requirements")
    val requirements: GameRequirements? = null,
    
    @SerializedName("features")
    val features: List<String> = emptyList(),
    
    @SerializedName("releaseDate")
    val releaseDate: String = "",
    
    @SerializedName("publisher")
    val publisher: String = "",
    
    @SerializedName("featured")
    val featured: Int = 0  // API envía 0 o 1 como número, no booleano
) {
    // Propiedades de compatibilidad con el código existente
    val name: String get() = title
    val imageUrl: String get() = image
    val isFeatured: Boolean get() = featured == 1  // Convertir a booleano cuando sea necesario
}

data class GameRequirements(
    @SerializedName("os")
    val os: String = "",
    
    @SerializedName("processor")
    val processor: String = "",
    
    @SerializedName("memory")
    val memory: String = "",
    
    @SerializedName("graphics")
    val graphics: String = "",
    
    @SerializedName("storage")
    val storage: String = ""
)
