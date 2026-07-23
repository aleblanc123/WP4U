package com.example.wp4u.database.dao

import com.example.wp4u.database.model.Category
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

/**
 * Category data access object.
 *
 * Used to insert new categories; Update existing categories; Select a category name;
 * Select a category description; Select all categories (array).
 */

@Dao
interface CategoryDAO {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category): Int //Adds new category that holds related images.

    @Update
    suspend fun updateCategory(category: Category) //Updates pre-existing category information.

    @Query ("SELECT category_name FROM CATEGORY WHERE id=:id")
    suspend fun getCategoryNameById(id: Int): Category? //Gets single category name (by id).

    @Query ("SELECT description FROM CATEGORY WHERE id=:id")
    suspend fun getCategoryDescById(id: Int): Category? //Gets single category description (by id).

    @Query ("SELECT * FROM CATEGORY")
    suspend fun getAllCategories(): Array<Category> //Gets all category names (array).
}