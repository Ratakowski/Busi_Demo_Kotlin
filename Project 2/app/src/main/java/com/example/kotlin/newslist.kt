import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityNewsListBinding
import com.example.newsapp.db.NewsDatabase
import com.example.newsapp.model.News

class NewsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsListBinding
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        fetchNews()
        setupLoginButton()
        setupCategoryMenu()
        setupLogoutButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(this::onNewsClicked, this::onDeleteNews)
        binding.recyclerView.adapter = newsAdapter
    }

    private fun fetchNews() {
        // Fetch news from database
        val newsListFromDb = NewsDatabase.getInstance(this).newsDao().getAllNews()

        // Update UI with news list
        newsAdapter.submitList(newsListFromDb)
    }

    private fun onNewsClicked(news: News) {
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.EXTRA_TITLE, news.title)
        intent.putExtra(NewsDetailActivity.EXTRA_IMAGE, news.image)
        intent.putExtra(NewsDetailActivity.EXTRA_CONTENT, news.content)
        startActivity(intent)
    }

    private fun onDeleteNews(news: News) {
        // Delete news from database
        NewsDatabase.getInstance(this).newsDao().deleteNews(news)

        // Fetch news again to update UI
        fetchNews()
    }

    private fun setupLoginButton() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupCategoryMenu() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_category_news -> {
                    // Change category to "News"
                    fetchNewsByCategory("News")
                    true
                }
                R.id.menu_category_recruitment -> {
                    // Change category to "Recruitment"
                    fetchNewsByCategory("Recruitment")
                    true
                }
                R.id.menu_category_training -> {
                    // Change category to "Training"
                    fetchNewsByCategory("Training")
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchNewsByCategory(category: String) {
        // Fetch news from database by category
        val newsListFromDb = NewsDatabase.getInstance(this).newsDao().getNewsByCategory(category)

        // Update UI with news list
        newsAdapter.submitList(newsListFromDb)
    }

    private fun setupLogoutButton() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_logout -> {
                    // Handle logout action
                    true
                }
                else -> false
            }
        }
    }
}
