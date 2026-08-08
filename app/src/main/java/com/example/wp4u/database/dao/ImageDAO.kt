package com.example.wp4u.database.dao

import com.example.wp4u.database.model.Image
import androidx.lifecycle.LiveData
import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

/**
 * Image data access object.
 *
 * Used to insert new images; Update existing images; Delete images;
 * Select an image; Select all images from a category (live, ordered).
 *
 * Demo 4 changes (to support the Room-backed BoardRepository):
 *  - getImagesForCategory replaces getAllCategories: correct name, returns
 *    LiveData so boards update automatically, orders by display_order so
 *    the saved arrangement is what the user sees, and filters by user_fk
 *    so each user sees only their own board.
 *  - deleteImage added for the Delete Image use case.
 *  - updateImages added so a drag-reorder writes all new positions in one
 *    bulk operation.
 *  - getMaxDisplayOrder added so new uploads go to the end of the board.
 */
@Dao
interface ImageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(image: Image): Long //Adds newly uploaded image.

    @Update
    suspend fun updateImage(image: Image) //Updates pre-uploaded image information.

    @Update
    suspend fun updateImages(images: List<Image>) //Bulk update, used for reorder.

    @Delete
    suspend fun deleteImage(image: Image) //Removes an image from its board.

    @Query("SELECT * FROM IMAGE WHERE imagePK=:id")
    suspend fun getImageById(id: Int): Image? //Gets single image.

    @Query("SELECT * FROM IMAGE WHERE category_fk=:categoryId AND user_fk=:userId ORDER BY display_order")
    fun getImagesForCategory(categoryId: Int, userId: Int): LiveData<List<Image>> //Live, ordered images of one category for one user.

    @Query("SELECT MAX(display_order) FROM IMAGE WHERE category_fk=:categoryId AND user_fk=:userId")
    suspend fun getMaxDisplayOrder(categoryId: Int, userId: Int): Int? //Highest position on one user's board (null if empty).
}
