package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = UserDatabaseHelper(this)
        preferenceHelper = PreferenceHelper(this)

        val emailEditText = findViewById<EditText>(R.id.loginEmailInput)
        val passwordEditText = findViewById<EditText>(R.id.loginPasswordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (dbHelper.checkUser(email, password)) {

                preferenceHelper.saveUserEmail(email)


                val intent = Intent(this, QuizSelectionActivity::class.java)
                intent.putExtra("USER_EMAIL", email) // Pass the email to the next activity
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        val signup = findViewById<Button>(R.id.signupButton)
        signup.setOnClickListener{
            val intent = Intent(this, Signupactivity::class.java)
            startActivity(intent)
        }
    }
}
