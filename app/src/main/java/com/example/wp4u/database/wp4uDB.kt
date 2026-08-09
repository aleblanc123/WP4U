package com.example.wp4u.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.Database
import androidx.room3.DaoReturnTypeConverters
import androidx.room3.RoomDatabase
import androidx.room3.livedata.LiveDataDaoReturnTypeConverter
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.wp4u.database.model.*
import com.example.wp4u.database.dao.*

/**
 * The actual WP4U database.
 *
 * Database entities are every database model (category, image, user); Each corresponding DAO is present.
 * Object returns a singleton instance of the database. Creates single instance if not already created.
 *
 * Demo 4 changes:
 *  - LiveData DAO return types registered via a return type converter. The
 *    project uses Room 3.0 (released July 2026), which moved LiveData support
 *    out of the core runtime into the room3-livedata artifact.
 *  - A creation callback seeds the five pre-made categories from the Demo 2
 *    design the first time the database file is created on a device. Fixed
 *    primary keys (1-5) are used so the bundled sample images in assets/seed/
 *    can reference stable category ids. Room 3 callbacks receive a
 *    SQLiteConnection (the SupportSQLite APIs were removed in 3.0).
 *
 * NOTE: the callback only fires when the database file is CREATED. On any
 * device/emulator that already ran an earlier build, uninstall the app once
 * so the database is recreated with the seed rows.
 */
@Database(entities = [Category::class, Image::class, User::class], version = 1)
@DaoReturnTypeConverters(LiveDataDaoReturnTypeConverter::class)
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
                    )
                        .addCallback(SeedCallback)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

        /** Inserts the five pre-made categories on first creation. */
        private val SeedCallback = object : Callback() {
            override suspend fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
                connection.execSQL(
                    "INSERT INTO CATEGORY (categoryPK, category_name, description) VALUES " +
                        "(1, 'Wedding Dresses', '')," +
                        "(2, 'Churches & Venues', '')," +
                        "(3, 'Food & Catering', '')," +
                        "(4, 'Flowers & Decor', '')," +
                        "(5, 'Invitations', '')"
                )
            }
        }
    }
}
