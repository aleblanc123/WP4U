package com.example.wp4u.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.sql.Time

/**
 * Category that holds images.
 *
 */

@Entity(tableName = "CATEGORY")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryPK: Int = 0,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "description") val description: String
)
