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
import com.example.wp4u.data.AuthRepository
import com.example.wp4u.database.WP4UDatabase
import com.example.wp4u.ui.categories.CategoriesActivity
import kotlinx.coroutines.launch

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        val emailEdt = findViewById<EditText>(R.id.createEmail)
        val usernameEdt = findViewById<EditText>(R.id.createUsername)
        val passwordEdt = findViewById<EditText>(R.id.createPassword)
        val createBtn = findViewById<Button>(R.id.createAccount)
        val signIn = findViewById<Button>(R.id.signIn)

        val database = WP4UDatabase.getInstance(this)
        val authRepository = AuthRepository(database.UserDAO())

        createBtn.setOnClickListener {
            val username = usernameEdt.text.toString()
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()
            if (TextUtils.isEmpty(emailEdt.text.toString()) || TextUtils.isEmpty(usernameEdt.text.toString()) || TextUtils.isEmpty(
                    passwordEdt.text.toString()
                )
            ) {

                Toast.makeText(
                    this@CreateAccount,
                    "Please Enter Email, Username and Password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {

                val result = authRepository.createAccount(username, email, password)
                if (result.isSuccess) {
                    val i = Intent(this@CreateAccount, CategoriesActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(
                        this@CreateAccount,
                        result.exceptionOrNull()?.message ?: "Failed to create account",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        signIn.setOnClickListener {
            val intent = Intent(this@CreateAccount, Login::class.java)
            startActivity(intent)
        }
        }
}
