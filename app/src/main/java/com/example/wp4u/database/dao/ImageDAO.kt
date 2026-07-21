package com.example.wp4u.database.dao

import com.example.wp4u.database.model.Image
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query

@Dao
interface ImageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(image: Image): Int

    @Query("SELECT * FROM IMAGE WHERE id=:id")
    suspend fun getImageById(id: Int): Image?
}