package com.example.wp4u.model

/**
 * One image on a vision board.
 *
 * The image file itself lives in the app's internal storage; only its
 * absolute path is stored here. This matches the Demo 2 design decision
 * (files on disk, paths in the database) and keeps the DB small and fast.
 *
 * NOTE for the data layer: as a Room entity this becomes
 * @Entity(tableName = "images") with a @ForeignKey to Category(id).
 * [position] drives the display order inside a category (0-based) and
 * will support the reorder feature in Part 4.
 */
data class BoardImage(
    val id: Long,
    val categoryId: Long,
    val filePath: String,
    val position: Int
)
