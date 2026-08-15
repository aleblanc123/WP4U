package com.example.wp4u.data

import android.content.Context
import com.example.wp4u.database.WP4UDatabase

/**
 * Minimal service locator so every screen shares ONE instance of each
 * repository.
 *
 * Demo 4:
 *  - repository returns the Room-backed implementation.
 *  - authRepository is shared (one instance signs users in AND is read by
 *    every other screen), and is wired with the SampleImageSeeder so new
 *    accounts start with the curated sample boards.
 *
 * init() must be called once before anything else - WP4UApp does this at
 * application startup.
 */
object ServiceLocator {

    private lateinit var appContext: Context
    private lateinit var database: WP4UDatabase

    fun init(context: Context) {
        if (!::database.isInitialized) {
            appContext = context.applicationContext
            database = WP4UDatabase.getInstance(context.applicationContext)
        }
    }

    val repository: BoardRepository by lazy {
        RoomBoardRepository(database.CategoryDAO(), database.ImageDAO())
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(
            database.UserDAO(),
            SampleImageSeeder(appContext, database.ImageDAO())
        )
    }
}
