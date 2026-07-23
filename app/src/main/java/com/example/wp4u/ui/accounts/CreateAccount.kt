package com.example.wp4u.ui.accounts

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wp4u.R

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        val emailEdt = findViewById<EditText>(R.id.createEmail)
        val usernameEdt = findViewById<EditText>(R.id.createUsername)
        val passwordEdt = findViewById<EditText>(R.id.createPassword)
        val createBtn = findViewById<Button>(R.id.createAccount)
        val signIn = findViewById<Button>(R.id.signIn)

        createBtn.setOnClickListener {
            if (TextUtils.isEmpty(emailEdt.text.toString()) || TextUtils.isEmpty(usernameEdt.text.toString()) || TextUtils.isEmpty(
                    passwordEdt.text.toString()
                )
            ) {

                Toast.makeText(
                    this@CreateAccount,
                    "Please Enter Email, Username and Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        signIn.setOnClickListener {
            val intent = Intent(this@CreateAccount, Login::class.java)
            startActivity(intent)
        }
    }
}
      //  override fun onStart(){
         //   super.onStart()
        //    if(email != null && username != null && password != null){
          //      val i = Intent(this@CreateAccount, Category::class.java)
           //     startActivity(i)
        //}
  //  }
