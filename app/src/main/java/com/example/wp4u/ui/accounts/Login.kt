package com.example.wp4u.ui.accounts

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wp4u.R
import com.example.wp4u.data.ServiceLocator
import com.example.wp4u.ui.categories.CategoriesActivity
import kotlinx.coroutines.launch

/**
 * Sign-in screen and the launcher activity for WP4U.
 *
 * This is the activity declared with the LAUNCHER intent filter in
 * `AndroidManifest.xml`, so it is the first screen the user sees. It checks the
 * supplied credentials through [com.example.wp4u.data.AuthRepository] and, on
 * success, opens [CategoriesActivity].
 *
 * Signing in matters beyond access control: vision boards are user-specific, so the
 * signed-in user's id is what every later image operation is recorded against.
 */
class Login : AppCompatActivity() {

    /**
     * Inflates the sign-in layout, binds the input fields, and wires up the
     * "login" and "sign up" buttons.
     *
     * @param savedInstanceState state saved by a previous instance of this activity,
     *                           or `null` when the activity is created for the first time.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Input fields and buttons defined in res/layout/login.xml.
        val userEdt = findViewById<EditText>(R.id.inputUsername)
        val pwordEdt = findViewById<EditText>(R.id.inputPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val signUp = findViewById<Button>(R.id.signUp)

        // Demo 4 fix: use the ONE shared AuthRepository so the signed-in
        // user is visible to every other screen (uploads record their id).
        val authRepository = ServiceLocator.authRepository

        // Validates both fields, then attempts to sign the user in.
        loginBtn.setOnClickListener {
            val username = userEdt.text.toString()
            val password = pwordEdt.text.toString()
            // Both fields are required; stop here and warn if either is blank.
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    this@Login,
                    "Please Enter Username and Password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            // lifecycleScope ties the coroutine to this activity, so the database
            // lookup is cancelled automatically if the screen is destroyed.
            lifecycleScope.launch {
                val result = authRepository.signIn(username, password)
                if (result.isSuccess) {
                    // Credentials accepted - open the user's vision board categories.
                    val i = Intent(this@Login, CategoriesActivity::class.java)
                    startActivity(i)
                } else {
                    // Unknown username or wrong password.
                    Toast.makeText(
                        this@Login,
                        result.exceptionOrNull()?.message ?: "Login attempt failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        // First-time users go to the registration screen instead.
        signUp.setOnClickListener {
            val intent = Intent(this@Login, CreateAccount::class.java)
            startActivity(intent)
        }
    }
}