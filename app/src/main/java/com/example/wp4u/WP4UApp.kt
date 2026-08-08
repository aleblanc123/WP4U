package com.example.wp4u

import android.app.Application
import com.example.wp4u.data.ServiceLocator

/**
 * Application entry point. Initializes the ServiceLocator once, before any
 * Activity runs, so every screen shares the same database-backed
 * repositories.
 *
 * Registered in AndroidManifest.xml via android:name=".WP4UApp".
 */
class WP4UApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}
