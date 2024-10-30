package com.example.quizgame

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Looper

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val quizType = intent.getStringExtra("quizType") ?: "unknown"
        val email = intent.getStringExtra("USER_EMAIL")
        val username = intent.getStringExtra("username")


        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        scoreTextView.text = "You scored $score out of $totalQuestions!"


        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val usernameTextView = findViewById<TextView>(R.id.usernameTextView)
        emailTextView.text = "Email: $email"
        usernameTextView.text = "Username: $username"


        val dbHelper = UserDatabaseHelper(this)


        if (username != null) {
            val isScoreAdded = dbHelper.addLeaderboardScore(username, score, quizType)
            if (isScoreAdded) {
                Toast.makeText(this, "Score saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save score.", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate back to QuizSelectionActivity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val usernameFromDb = dbHelper.getUsername(email ?: "")
            val intent = Intent(this, QuizSelectionActivity::class.java)
            intent.putExtra("USERNAME", usernameFromDb)
            intent.putExtra("USER_EMAIL", email)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
