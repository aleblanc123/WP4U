package com.example.wp4u.data

import android.content.Context
import com.example.wp4u.database.WP4UDatabase

/**
 * Minimal service locator so every screen shares ONE instance of each
 * repository.
 *
 * Demo 4 changes:
 *  - repository now returns the Room-backed implementation (the swap the
 *    interface was designed for; FakeBoardRepository is deleted).
 *  - authRepository added and shared. Previously each Activity constructed
 *    its own AuthRepository, so the instance that signed the user in was
 *    not the instance other screens read currentUser from - it was always
 *    null outside the login screen. One shared instance fixes that and is
 *    what lets uploads record the real signed-in user's id.
 *
 * init() must be called once before anything else - WP4UApp does this at
 * application startup.
 */
object ServiceLocator {

    private lateinit var database: WP4UDatabase

    fun init(context: Context) {
        if (!::database.isInitialized) {
            database = WP4UDatabase.getInstance(context.applicationContext)
        }
    }

    val repository: BoardRepository by lazy {
        RoomBoardRepository(database.CategoryDAO(), database.ImageDAO())
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(database.UserDAO())
    }
}
