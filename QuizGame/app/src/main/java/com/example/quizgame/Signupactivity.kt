package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Signupactivity : AppCompatActivity() {
    lateinit var dbHelper: UserDatabaseHelper
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signupactivity)

        dbHelper = UserDatabaseHelper(this)
        preferenceHelper = PreferenceHelper(this)


        val savedEmail = preferenceHelper.getUserEmail()
        if (savedEmail != null) {
            // User is already logged in, redirect to QuizSelectionActivity
            val intent = Intent(this, QuizSelectionActivity::class.java)
            intent.putExtra("USER_EMAIL", savedEmail)
            startActivity(intent)
            finish()
            return
        }

        val signupButton = findViewById<Button>(R.id.signupButton)

        signupButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.signupEmailInput).text.toString()
            val username = findViewById<EditText>(R.id.signupUsernameInput).text.toString()
            val password = findViewById<EditText>(R.id.signupPasswordInput).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.signupConfirmPasswordInput).text.toString()


            if (!isValidEmail(email)) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val isInserted = dbHelper.addUser(username, email, password, null)
            if (isInserted) {
                // Save user login in preferences
                preferenceHelper.saveUserEmail(email)

                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, QuizSelectionActivity::class.java)
                intent.putExtra("USER_EMAIL", email)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Sign up failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        val loginbtn = findViewById<Button>(R.id.loginButton)
        loginbtn.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
