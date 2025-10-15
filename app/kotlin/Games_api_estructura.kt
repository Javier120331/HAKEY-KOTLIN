import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int,
    val title: String,
    val price: Double,
    // @SerializedName se usa cuando el nombre en el JSON es diferente al de la variable
    @SerializedName("original_price")
    val originalPrice: Double,
    val discount: Double,
    val image: String,
    val category: String,
    val platform: List<String>,
    val rating: Float,
    val description: String,
    val requirements: Requirements,
    val features: List<String>,
    @SerializedName("release_date")
    val releaseDate: String,
    val publisher: String,
    val featured: Boolean
)

data class Requirements(
    val os: String,
    val processor: String,
    val memory: String,
    val graphics: String,
    val storage: String
)