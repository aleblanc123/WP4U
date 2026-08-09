package com.example.wp4u.data

import androidx.lifecycle.LiveData
import com.example.wp4u.database.model.*

/**
 * The contract between the UI layer and the data layer.
 *
 * ViewModels talk ONLY to this interface (as per the Demo 2 architecture),
 * so the in-memory FakeBoardRepository could be swapped for the Room-backed
 * implementation without touching any UI code. As of Demo 4 the Room-backed
 * [RoomBoardRepository] is the live implementation.
 */
interface BoardRepository {

    /** All categories, for the browsing screen. */
    fun getCategories(): LiveData<List<Category>>

    /**
     * The signed-in user's images inside one category, ordered by display
     * position. Boards are per-user: the IMAGE table's user foreign key
     * scopes each user's uploads to their own boards.
     */
    fun getImagesForCategory(categoryPK: Int, userFK: Int): LiveData<List<Image>>

    /**
     * Adds an image (already copied into internal storage) to the end
     * of the given category's board, owned by the signed-in user.
     */
    suspend fun addImage(categoryPK: Int, filePath: String, userFK: Int)

    /** Removes an image from its board and deletes its file from storage. */
    suspend fun deleteImage(image: Image)

    /**
     * Swaps an image's picture for a new one (already copied into internal
     * storage) while keeping its position on the board. The old file is
     * deleted from storage.
     */
    suspend fun replaceImage(image: Image, newFilePath: String)

    /**
     * Persists a new board order. The list is the images in their new
     * on-screen order; displayOrder is rewritten to match the list index.
     */
    suspend fun reorderImages(images: List<Image>)
}
