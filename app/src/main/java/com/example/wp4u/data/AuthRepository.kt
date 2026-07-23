package com.example.wp4u.data

import com.example.wp4u.database.model.User
import java.security.MessageDigest
import com.example.wp4u.database.dao.UserDAO

class AuthRepository(private val userDao: UserDAO) {

    var currentUser: User? = null
        private set

    suspend fun createAccount(username: String, email: String, password: String): Result<User> {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(Exception("Account already exists"))
        }

        val newUser = User(
            username = username,
            email = email,
            passwordHash = hashPassword(password),
            createdAt = System.currentTimeMillis()
        )

        userDao.insert(newUser)
        return Result.success(newUser)
    }

    suspend fun signIn(username: String, password: String): Result<User> {
        val user = userDao.getUserByUsername(username)
            ?: return Result.failure(Exception("No account found"))

        return if (user.passwordHash == hashPassword(password)) {
            currentUser = user
            Result.success(user)
        } else {
            Result.failure(Exception("Incorrect password"))
        }
    }

    fun signOut(){
    currentUser = null
}

    private fun hashPassword(password: String): String{
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") {"%02x".format(it)}
    }
}


