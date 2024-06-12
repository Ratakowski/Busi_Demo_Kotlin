import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchNews()
    }

    private fun fetchNews() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.apiService.fetchNews()
                val newsList = response.data
                // Handle the fetched news list
                Log.d("MainActivity", "Fetched news: $newsList")
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to fetch news", e)
            }
        }
    }
}
