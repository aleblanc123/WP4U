package com.example.wp4u.data

import com.example.wp4u.database.model.User
import java.security.MessageDigest
import com.example.wp4u.database.dao.UserDAO

class AuthRepository(private val userDao: UserDAO) {

    var currentUser: User? = null
        private set

    suspend fun createAccount(username: String, email: String, password: String): Result<User> {
        val existing = userDao.checkIfEmailExists(email)
        if (existing != null) {
            return Result.failure(Exception("Account already exists"))
        }

        val newUser = User(
            username = username,
            email = email,
            passwordHash = hashPassword(password),
            createdAt = System.currentTimeMillis()
        )

        // Demo 4 fix: capture the generated primary key. Previously the
        // returned row id was discarded, so currentUser kept userPK = 0 and
        // any image insert for a brand-new account would violate the
        // IMAGE -> USER foreign key.
        val newId = userDao.insert(newUser)
        val created = newUser.copy(userPK = newId.toInt())
        currentUser = created
        return Result.success(created)
    }

    suspend fun signIn(username: String, password: String): Result<User> {
        // The query already matches on username AND password hash, so a
        // returned row IS a successful authentication (the second in-memory
        // hash comparison that used to be here was redundant).
        val user = userDao.getUserByUserPass(username, hashPassword(password))
            ?: return Result.failure(Exception("Incorrect username or password"))

        currentUser = user
        return Result.success(user)
    }

    fun signOut() {
        currentUser = null
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
