package com.example.wp4u.database.dao

import com.example.wp4u.database.model.Image
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.wp4u.database.model.Category

/**
 * Image data access object.
 *
 * Used to insert new images; Update existing images; Select an image;
 * Select all images from a category.
 */

@Dao
interface ImageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(image: Image): Long //Adds newly uploaded image.

    @Update
    suspend fun updateImage(image: Image) //Updates pre-uploaded image information.

    @Query("SELECT * FROM IMAGE WHERE imagePK=:id")
    suspend fun getImageById(id: Int): Image? //Gets single image.

    @Query ("SELECT * FROM IMAGE WHERE category_fk=:categoryId")
    suspend fun getAllCategories(categoryId: Int): Array<Image> //Gets all images from a specific category.
}