package com.example.wp4u.database.dao

import com.example.wp4u.database.model.Category
import androidx.lifecycle.LiveData
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

/**
 * Category data access object.
 *
 * Used to insert new categories; Update existing categories; Select a category name;
 * Select a category description; Select all categories.
 *
 * Demo 4 change: getCategoriesLive added so the browsing screen observes
 * the categories as LiveData (the Room-backed BoardRepository returns it
 * directly). Ordered by primary key so the seeded categories appear in the
 * Demo 2 design order.
 */
@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category): Long //Adds new category that holds related images.

    @Update
    suspend fun updateCategory(category: Category) //Updates pre-existing category information.

    @Query("SELECT category_name FROM CATEGORY WHERE categoryPK=:id")
    suspend fun getCategoryNameById(id: Int): String? //Gets single category name (by id).

    @Query("SELECT description FROM CATEGORY WHERE categoryPK=:id")
    suspend fun getCategoryDescById(id: Int): String? //Gets single category description (by id).

    @Query("SELECT * FROM CATEGORY")
    suspend fun getAllCategories(): Array<Category> //Gets all category names (array).

    @Query("SELECT * FROM CATEGORY ORDER BY categoryPK")
    fun getCategoriesLive(): LiveData<List<Category>> //Live categories for the browsing screen.
}
