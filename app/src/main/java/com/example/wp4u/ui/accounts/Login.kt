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
import com.example.wp4u.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val userEdt = findViewById<EditText>(R.id.inputUsername)
        val pwordEdt = findViewById<EditText>(R.id.inputPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val signUp = findViewById<Button>(R.id.signUp)

        loginBtn.setOnClickListener {
            if (TextUtils.isEmpty(userEdt.text.toString()) || TextUtils.isEmpty(
                    pwordEdt.text.toString()
                )
            ){

                Toast.makeText(
                    this@Login,
                    "Please Enter Email and Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        signUp.setOnClickListener {
            val intent = Intent(this@Login, CreateAccount::class.java)
            startActivity(intent)
        }
    }
    //  override fun onStart(){
    //   super.onStart()
    //    if(username != null && password != null){
    //      val i = Intent(this@Login, Category::class.java)
    //     startActivity(i)
        //    }
       // }
}