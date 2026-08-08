package com.example.wp4u.data

import androidx.lifecycle.LiveData
import com.example.wp4u.database.dao.CategoryDAO
import com.example.wp4u.database.dao.ImageDAO
import com.example.wp4u.database.model.Category
import com.example.wp4u.database.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * The Room-backed implementation of [BoardRepository] (Demo 4).
 *
 * Replaces FakeBoardRepository: same contract, but every change is written
 * to the WP4U database, so boards survive an application restart. Image
 * files live in internal storage; the database stores only their paths
 * (Demo 2 design decision), so delete/replace also clean up the old file
 * on disk to avoid orphaned files accumulating.
 */
class RoomBoardRepository(
    private val categoryDao: CategoryDAO,
    private val imageDao: ImageDAO
) : BoardRepository {

    override fun getCategories(): LiveData<List<Category>> =
        categoryDao.getCategoriesLive()

    override fun getImagesForCategory(categoryPK: Int, userFK: Int): LiveData<List<Image>> =
        imageDao.getImagesForCategory(categoryPK, userFK)

    override suspend fun addImage(categoryPK: Int, filePath: String, userFK: Int) {
        // New images go to the end of the board.
        val maxOrder = imageDao.getMaxDisplayOrder(categoryPK, userFK) ?: -1
        imageDao.insertImage(
            Image(
                userFK = userFK,
                categoryFK = categoryPK,
                filePath = filePath,
                displayOrder = maxOrder + 1,
                uploadedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteImage(image: Image) {
        imageDao.deleteImage(image)
        deleteFileQuietly(image.filePath)
    }

    override suspend fun replaceImage(image: Image, newFilePath: String) {
        val oldPath = image.filePath
        imageDao.updateImage(
            image.copy(
                filePath = newFilePath,
                uploadedAt = System.currentTimeMillis()
            )
        )
        deleteFileQuietly(oldPath)
    }

    override suspend fun reorderImages(images: List<Image>) {
        // Rewrite displayOrder to match the new on-screen order in one
        // bulk update, so a single drag is a single database transaction.
        imageDao.updateImages(
            images.mapIndexed { index, image -> image.copy(displayOrder = index) }
        )
    }

    /** File cleanup is best-effort: a leftover file must never crash the app. */
    private suspend fun deleteFileQuietly(path: String) =
        withContext(Dispatchers.IO) {
            runCatching { File(path).delete() }
        }
}
