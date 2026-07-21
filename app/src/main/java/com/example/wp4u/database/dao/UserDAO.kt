package com.example.wp4u.database.dao

import com.example.wp4u.database.model.User
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Int

    @Query("SELECT * FROM USER WHERE id=:id")
    suspend fun getUserById(id: Int): User?
}