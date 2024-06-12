import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("data")
    val data: List<News>
)

data class News(
    val id: String,
    val title: String,
    val description: String,
    val image: String
)
