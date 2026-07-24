package com.example.wp4u.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.wp4u.database.model.*
import com.example.wp4u.database.dao.*

/**
 * The actual WP4U database.
 *
 * Database entities are every database model (category, image, user); Each corresponding DAO is present.
 * Object returns a singleton instance of the database. Creates single instance if not already created.
 */

@Database(entities = [Category::class, Image::class, User::class], version = 1)
abstract class WP4UDatabase : RoomDatabase() {
    abstract fun CategoryDAO(): CategoryDAO
    abstract fun ImageDAO(): ImageDAO
    abstract fun UserDAO(): UserDAO

    // Ensures single instance of database (singleton pattern).
    companion object {
        @Volatile
        private var INSTANCE: WP4UDatabase? = null

        fun getInstance(context: Context): WP4UDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WP4UDatabase::class.java,
                        "wp4uDB.db"
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}