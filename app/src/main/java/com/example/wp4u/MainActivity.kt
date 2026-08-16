package com.example.wp4u

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room3.Room

/**
 * Template activity created automatically when the Android Studio project was
 * first generated.
 *
 * WP4U's actual entry point is [com.example.wp4u.ui.accounts.Login], which is the
 * activity declared with the LAUNCHER intent filter in `AndroidManifest.xml`. This
 * class is retained as the default scaffold: it inflates `activity_main` and applies
 * system bar insets so content is not drawn underneath the status and navigation bars.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Sets up the screen when the activity is created.
     *
     * Enables edge-to-edge drawing, inflates the layout, and installs a listener that
     * pads the root view by the size of the system bars.
     *
     * @param savedInstanceState state saved by a previous instance of this activity,
     *                           or `null` when the activity is created for the first time.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Allows the app to draw behind the status and navigation bars.
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Because the app now draws edge to edge, the root view is padded by the
        // measured size of the system bars so no content is hidden behind them.
        // The insets are returned unchanged so child views can also consume them.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}