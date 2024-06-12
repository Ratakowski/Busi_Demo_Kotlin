import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityNewsDetailBinding

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_CONTENT = "extra_content"
    }

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(EXTRA_TITLE) ?: "News Detail"
        val image = intent.getStringExtra(EXTRA_IMAGE) ?: ""
        val content = intent.getStringExtra(EXTRA_CONTENT) ?: ""

        setupViews(title, image, content)
    }

    private fun setupViews(title: String, image: String, content: String) {
        supportActionBar?.title = title

        val imageView: ImageView = binding.imageView
        if (image.isNotEmpty()) {
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.error_image)
        }

        val titleTextView: TextView = binding.titleTextView
        titleTextView.text = title

        val contentTextView: TextView = binding.contentTextView
        contentTextView.text = content
    }
}
