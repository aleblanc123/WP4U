package com.example.wp4u.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import androidx.room3.Index

/**
 * Image on the vision board.
 *
 * The image file itself lives in the app's internal storage; only its
 * absolute path is stored here. This matches the Demo 2 design decision
 * (files on disk, paths in the database) and keeps the DB small and fast.
 */
@Entity(tableName = "IMAGE",
    indices = [Index("user_fk"), Index("category_fk")],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            childColumns = ["user_fk"],
            parentColumns = ["userPK"]
        ),
        ForeignKey(
            entity = Category::class,
            childColumns = ["category_fk"],
            parentColumns = ["categoryPK"]
    )])
data class Image(
    @PrimaryKey(autoGenerate = true) val imagePK: Int = 0,
    @ColumnInfo(name = "user_fk") val userFK: Int,
    @ColumnInfo(name = "category_fk") val categoryFK: Int,
    @ColumnInfo(name = "file_path") val filePath: String,
    @ColumnInfo(name = "display_order") val displayOrder: Int, // will support reorder feature in pt 4 (0-based)
    @ColumnInfo(name = "uploaded_at") val uploadedAt: Long,
)
