package com.example.wp4u.ui.accounts

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.wp4u.R
import com.example.wp4u.data.AuthRepository
import com.example.wp4u.database.WP4UDatabase
import com.example.wp4u.ui.categories.CategoriesActivity
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val userEdt = findViewById<EditText>(R.id.inputUsername)
        val pwordEdt = findViewById<EditText>(R.id.inputPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val signUp = findViewById<Button>(R.id.signUp)

        val database = WP4UDatabase.getInstance(this)
        val authRepository = AuthRepository(database.UserDAO())

        loginBtn.setOnClickListener {
            val username = userEdt.text.toString()
            val password = pwordEdt.text.toString()
            if (TextUtils.isEmpty(userEdt.text.toString()) || TextUtils.isEmpty(
                    pwordEdt.text.toString()
                )
            ) {

                Toast.makeText(
                    this@Login,
                    "Please Enter Username and Password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {

                val result = authRepository.signIn(username, password)
                if (result.isSuccess) {
                    val i = Intent(this@Login, CategoriesActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(
                        this@Login,
                        result.exceptionOrNull()?.message ?: "Login attempt failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        signUp.setOnClickListener {
            val intent = Intent(this@Login, CreateAccount::class.java)
            startActivity(intent)
        }
    }

}