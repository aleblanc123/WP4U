package com.example.wp4u.data

import androidx.lifecycle.LiveData
import com.example.wp4u.model.BoardImage
import com.example.wp4u.model.Category

/**
 * The contract between the UI layer and the data layer.
 *
 * ViewModels talk ONLY to this interface (as per the Demo 2 architecture),
 * so the in-memory FakeBoardRepository can be swapped for the Room-backed
 * implementation without touching any UI code.
 *
 * DAO signature contract (for the Room implementation):
 *   @Query("SELECT * FROM categories ORDER BY name")
 *   fun getCategories(): LiveData<List<Category>>
 *
 *   @Query("SELECT * FROM images WHERE categoryId = :categoryId ORDER BY position")
 *   fun getImagesForCategory(categoryId: Long): LiveData<List<BoardImage>>
 *
 *   @Insert
 *   suspend fun insertImage(image: BoardImage)
 */
interface BoardRepository {

    /** All categories, for the browsing screen. */
    fun getCategories(): LiveData<List<Category>>

    /** Images inside one category, ordered by position. */
    fun getImagesForCategory(categoryId: Long): LiveData<List<BoardImage>>

    /**
     * Adds an image (already copied into internal storage) to the end
     * of the given category's board.
     */
    suspend fun addImage(categoryId: Long, filePath: String)
}
