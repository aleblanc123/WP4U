package com.example.wp4u.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.sql.Time

/**
 * Category that holds images — one row per vision board type.
 *
 * Room entity mapped to the `CATEGORY` table. WP4U ships with exactly five
 * categories, inserted with FIXED primary keys by the database creation callback in
 * `WP4UDatabase`:
 *
 *  1. Wedding Dresses
 *  2. Churches & Venues
 *  3. Food & Catering
 *  4. Flowers & Decor
 *  5. Invitations
 *
 * Those ids are deliberately fixed rather than left to chance: they match the
 * `assets/seed/<folder>` names, which is how `SampleImageSeeder` knows which board
 * each bundled sample image belongs to.
 *
 * @property categoryPK primary key of the category. Declared `autoGenerate = true`,
 *                      but in practice the five seeded rows are inserted with explicit
 *                      ids 1-5 so they stay stable across installs.
 * @property categoryName display name shown in the categories list, e.g. "Wedding Dresses".
 * @property description longer text describing the category. Currently empty for the
 *                       seeded rows; reserved for future UI.
 */
@Entity(tableName = "CATEGORY")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryPK: Int = 0,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "description") val description: String
)