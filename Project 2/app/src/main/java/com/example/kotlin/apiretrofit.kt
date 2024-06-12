import retrofit2.http.GET

interface ApiService {
    @GET("api/cnn-news/nasional")
    suspend fun fetchNews(): NewsResponse
}
