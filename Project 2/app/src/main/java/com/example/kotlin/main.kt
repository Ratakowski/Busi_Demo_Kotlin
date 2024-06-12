package com.example.simple_news_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch(Dispatchers.IO) {
            package com.example.simple_news_app

            import android.os.Bundle
                    import androidx.appcompat.app.AppCompatActivity
                    import androidx.lifecycle.lifecycleScope
                    import kotlinx.coroutines.Dispatchers
                    import kotlinx.coroutines.launch
                    import kotlinx.coroutines.withContext

            class MainActivity : AppCompatActivity() {

                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(R.layout.activity_main)

                    lifecycleScope.launch(Dispatchers.IO) {
                        myFunction()
                        withContext(Dispatchers.Main) {
                            // Initialize the UI, for example, setting the initial fragment
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, NewsListFragment())
                                .commit()
                        }
                    }
                }

                private suspend fun myFunction() {
                    val newsDatabase = NewsDatabase.getDatabase(this)

                    val newUser = User(
                        username = "satya",
                        password = "123210140"
                    )

                    newsDatabase.userDao().insertUser(newUser)

                    val user = newsDatabase.userDao().authenticate("satya", "123210140")

                    if (user != null) {
                        println("User authenticated: ${user.username}")
                    } else {
                        println("User not found or incorrect password")
                    }
                }
            }
