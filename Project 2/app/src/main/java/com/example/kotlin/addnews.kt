package com.example.simple_news_app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewsActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var summaryEditText: EditText
    private lateinit var imageEditText: EditText
    private lateinit var categorySpinner: Spinner
    private var newsId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_news)

        titleEditText = findViewById(R.id.edit_text_title)
        summaryEditText = findViewById(R.id.edit_text_summary)
        imageEditText = findViewById(R.id.edit_text_image)
        categorySpinner = findViewById(R.id.spinner_category)
        val saveButton: Button = findViewById(R.id.button_save)

        // Set up category spinner
        val categories = arrayOf("News", "Recruitment", "Training")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Check if editing an existing news item
        val news = intent.getParcelableExtra<News>("news")
        news?.let {
            newsId = it.id
            titleEditText.setText(it.title)
            summaryEditText.setText(it.summary)
            imageEditText.setText(it.image)
            val categoryPosition = categories.indexOf(it.category)
            categorySpinner.setSelection(categoryPosition)
        }

        saveButton.setOnClickListener {
            saveNews()
        }
    }

    private fun saveNews() {
        val title = titleEditText.text.toString()
        val summary = summaryEditText.text.toString()
        val image = imageEditText.text.toString()
        val category = categorySpinner.selectedItem.toString()

        val news = News(
            id = newsId,
            title = title,
            summary = summary,
            image = image,
            category = category
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val newsDatabase = NewsDatabase.getDatabase(this@AddNewsActivity)
            if (newsId == null) {
                newsDatabase.newsDao().insertNews(news)
            } else {
                newsDatabase.newsDao().updateNews(news)
            }
            finish() // Close the activity and go back
        }
    }
}
