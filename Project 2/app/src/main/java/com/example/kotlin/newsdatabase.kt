import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class News(
    val id: Int?,
    val title: String,
    val summary: String,
    val image: String,
    val category: String
)

class User(
    val id: Int?,
    val username: String,
    val password: String,
    val lastLoginLocation: String
)

class NewsDatabase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "news.db"

        private const val TABLE_NEWS = "news"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_SUMMARY = "summary"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_CATEGORY = "category"

        private const val TABLE_USERS = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_LAST_LOGIN_LOCATION = "last_login_location"

        private var instance: NewsDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NewsDatabase {
            return instance ?: NewsDatabase(context.applicationContext).also {
                instance = it
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_NEWS_TABLE = ("CREATE TABLE $TABLE_NEWS("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TITLE TEXT,"
                + "$COLUMN_SUMMARY TEXT,"
                + "$COLUMN_IMAGE TEXT,"
                + "$COLUMN_CATEGORY TEXT"
                + ")")

        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_USERNAME TEXT,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_LAST_LOGIN_LOCATION TEXT"
                + ")")

        db.execSQL(CREATE_NEWS_TABLE)
        db.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NEWS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun insertNews(news: News): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, news.title)
            put(COLUMN_SUMMARY, news.summary)
            put(COLUMN_IMAGE, news.image)
            put(COLUMN_CATEGORY, news.category)
        }
        val id = db.insert(TABLE_NEWS, null, values)
        db.close()
        return id
    }

    fun fetchNews(category: String): List<News> {
        val newsList = mutableListOf<News>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NEWS WHERE $COLUMN_CATEGORY = ?"
        val cursor = db.rawQuery(query, arrayOf(category))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                val summary = cursor.getString(cursor.getColumnIndex(COLUMN_SUMMARY))
                val image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                val category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
                val news = News(id, title, summary, image, category)
                newsList.add(news)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return newsList
    }

    fun updateNews(news: News): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, news.title)
            put(COLUMN_SUMMARY, news.summary)
            put(COLUMN_IMAGE, news.image)
            put(COLUMN_CATEGORY, news.category)
        }
        val affectedRows = db.update(TABLE_NEWS, values, "$COLUMN_ID = ?", arrayOf(news.id.toString()))
        db.close()
        return affectedRows
    }

    fun deleteNews(id: Int): Int {
        val db = writableDatabase
        val affectedRows = db.delete(TABLE_NEWS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return affectedRows
    }

    fun insertUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_LAST_LOGIN_LOCATION, user.lastLoginLocation)
        }
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    fun authenticate(username: String, password: String): User? {
        var user: User? = null
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val lastLoginLocation = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_LOGIN_LOCATION))
            user = User(id, username, password, lastLoginLocation)
        }
        cursor.close()
        db.close()
        return user
    }

    fun updateLoginLocation(userId: Int, location: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LAST_LOGIN_LOCATION, location)
        }
        val affectedRows = db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userId.toString()))
        db.close()
        return affectedRows
    }

    fun closeDatabase() {
        instance?.close()
        instance = null
    }
}
