package com.example.wp4u.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

/**
 * User account to keep user activity across devices.
 *
 */

@Entity(tableName = "USER")
data class User(
    @PrimaryKey(autoGenerate = true) val userPK: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password_hash") val passwordHash: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
)