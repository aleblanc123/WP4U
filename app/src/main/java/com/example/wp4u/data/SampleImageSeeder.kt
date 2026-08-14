package com.example.wp4u.data

import android.content.Context
import com.example.wp4u.database.dao.ImageDAO
import com.example.wp4u.database.model.Image

/**
 * Copies the curated sample images (bundled in assets/seed/) onto a new
 * user's boards when their account is created.
 *
 * Boards are per-user, so samples are seeded per account rather than once
 * globally: every new user starts with a populated set of vision boards.
 * Asset folders map to the category ids fixed by the database seed
 * callback. Image sources are credited in assets/seed/ATTRIBUTIONS.md.
 */
class SampleImageSeeder(
    private val context: Context,
    private val imageDao: ImageDAO
) {

    /** assets/seed/<folder> -> seeded categoryPK (see WP4UDatabase). */
    private val folderToCategory = mapOf(
        "dresses" to 1,
        "venues" to 2,
        "food" to 3,
        "flowers" to 4,
        "invitations" to 5
    )

    /**
     * Copies every bundled sample image into internal storage and inserts
     * an IMAGE row for [userPK]. Files are sorted so every account gets
     * the same starting order.
     */
    suspend fun seedForUser(userPK: Int) {
        for ((folder, categoryPK) in folderToCategory) {
            val fileNames = context.assets.list("seed/$folder")
                ?.sorted()
                ?: continue

            fileNames.forEachIndexed { index, fileName ->
                val path = ImageStorage.copyAssetToInternalStorage(
                    context, "seed/$folder/$fileName"
                ) ?: return@forEachIndexed // skip one unreadable file, keep going

                imageDao.insertImage(
                    Image(
                        userFK = userPK,
                        categoryFK = categoryPK,
                        filePath = path,
                        displayOrder = index,
                        uploadedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}
