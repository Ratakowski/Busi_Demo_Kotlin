data class User(
    val id: Int?,
    val username: String,
    val password: String,
    val lastLoginLocation: String?
) {
    companion object {
        fun fromMap(map: Map<String, Any?>): User {
            val id = map["id"] as Int?
            val username = map["username"] as String
            val password = map["password"] as String
            val lastLoginLocation = map["last_login_location"] as String?
            return User(id, username, password, lastLoginLocation)
        }
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "username" to username,
            "password" to password,
            "last_login_location" to lastLoginLocation
        )
    }
}
