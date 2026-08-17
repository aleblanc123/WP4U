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
 * Sign-up screen: registers a new WP4U account.
 *
 * Collects a username, email and password, hands them to
 * [com.example.wp4u.data.AuthRepository], and on success sends the user straight to
 * [CategoriesActivity]. Because vision boards are per-user, account creation is also
 * the point at which the bundled sample images are copied into the new account's
 * internal storage (the repository delegates that to `SampleImageSeeder`).
 *
 * Registration runs inside a coroutine because the underlying Room calls are
 * `suspend` functions and must not block the main thread.
 */
class CreateAccount : AppCompatActivity() {

    /**
     * Inflates the sign-up layout, binds the input fields, and wires up the
     * "create account" and "sign in" buttons.
     *
     * @param savedInstanceState state saved by a previous instance of this activity,
     *                           or `null` when the activity is created for the first time.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        // Input fields and buttons defined in res/layout/create_account.xml.
        val emailEdt = findViewById<EditText>(R.id.createEmail)
        val usernameEdt = findViewById<EditText>(R.id.createUsername)
        val passwordEdt = findViewById<EditText>(R.id.createPassword)
        val createBtn = findViewById<Button>(R.id.createAccount)
        val signIn = findViewById<Button>(R.id.signIn)

        // Demo 4 fix: use the ONE shared AuthRepository so the new account
        // (with its real generated id) is the user every screen sees.
        val authRepository = ServiceLocator.authRepository

        // Validates the three fields, then attempts registration.
        createBtn.setOnClickListener {
            val username = usernameEdt.text.toString()
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()
            // All three fields are required; stop here and warn if any is blank.
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(password)
            ) {
                Toast.makeText(
                    this@CreateAccount,
                    "Please Enter Email, Username and Password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            // lifecycleScope ties the coroutine to this activity, so the database
            // work is cancelled automatically if the screen is destroyed.
            lifecycleScope.launch {
                val result = authRepository.createAccount(username, email, password)
                if (result.isSuccess) {
                    // Account created (and sample boards seeded) - go to the categories list.
                    val i = Intent(this@CreateAccount, CategoriesActivity::class.java)
                    startActivity(i)
                } else {
                    // Typical failure: the email is already registered.
                    Toast.makeText(
                        this@CreateAccount,
                        result.exceptionOrNull()?.message ?: "Failed to create account",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        // For users who already have an account: switch to the sign-in screen.
        signIn.setOnClickListener {
            val intent = Intent(this@CreateAccount, Login::class.java)
            startActivity(intent)
        }
    }
}