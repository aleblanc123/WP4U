package com.example.wp4u.model

/**
 * A vision board category (e.g. "Wedding Dresses").
 *
 * NOTE for the data layer: when this becomes a Room entity, annotate with
 * @Entity(tableName = "categories") and mark [id] as @PrimaryKey(autoGenerate = true).
 * The fields themselves should not change - the UI layer depends on them.
 */
data class Category(
    val id: Long,
    val name: String
)
