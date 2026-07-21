package com.example.wp4u.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.wp4u.database.model.*
import com.example.wp4u.database.dao.*

@Database(entities = [Category::class, Image::class, User::class], version = 1)
abstract class MyRoomDB : RoomDatabase() {
    abstract fun CategoryDAO(): CategoryDAO
    abstract fun ImageDAO(): ImageDAO
    abstract fun UserDAO(): UserDAO
}