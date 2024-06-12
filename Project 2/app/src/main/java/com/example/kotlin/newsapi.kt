import com.google.gson.annotations.SerializedName

data class CallingApi(
    val title: String,
    val link: String,
    @SerializedName("contentSnippet") val contentSnippet: String,
    val isoDate: String,
    val image: Image
)

data class Image(
    val small: String
)
