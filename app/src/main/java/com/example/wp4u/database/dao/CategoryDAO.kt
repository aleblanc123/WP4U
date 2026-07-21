package com.example.wp4u.database.dao

import com.example.wp4u.database.model.Category
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category): Int

    @Query("SELECT * FROM CATEGORY WHERE id=:id")
    suspend fun getCategoryById(id: Int): Category?
}