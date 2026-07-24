package com.example.wp4u.database.dao

import com.example.wp4u.database.model.User
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Update
import androidx.room3.Delete
import androidx.room3.OnConflictStrategy
import androidx.room3.Query

/**
 * User data access object.
 *
 * Used to insert new users; Update existing users; Delete users; Select a user.
 */

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Int //Adds newly created user account.

    @Update
    suspend fun updateUser(user: User) //Updates pre-existing user account.

    @Delete
    suspend fun deleteUser(user: User) //Deletes a user account.

    @Query("SELECT * FROM USER WHERE id=:id")
    suspend fun getUserById(id: Int): User? //Gets single user account (by id).

    @Query("SELECT * FROM USER WHERE username=:username AND password_hash=:passwordHash")
    suspend fun getUserByUserPass(username: String, passwordHash: String): User? //Gets single user account (by username and password).

    @Query("SELECT * FROM USER WHERE email=:email AND password_hash=:passwordHash")
    suspend fun getUserByEmailPass(email: String, passwordHash: String): User? //Gets single user account (by email and password).

    @Query("Select * FROM USER WHERE email=:email")
    suspend fun checkIfEmailExists(email: String): User? //Checks if email is already registered to account in database

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long
}