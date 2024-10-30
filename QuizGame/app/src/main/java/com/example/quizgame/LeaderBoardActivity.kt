package com.example.quizgame

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LeaderBoardActivity : AppCompatActivity() {
    private lateinit var leaderboardTextView: TextView
    private lateinit var databaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)

        leaderboardTextView = findViewById(R.id.leaderboardTextView)
        databaseHelper = UserDatabaseHelper(this)


        val quizType = intent.getStringExtra("quizType")

        if (quizType != null) {

            displayLeaderboard(quizType)
        } else {
            Toast.makeText(this, "Invalid quiz type.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun displayLeaderboard(quizType: String) {
        val topScores = databaseHelper.getTopScores(quizType)
        val leaderboardBuilder = StringBuilder()

        leaderboardBuilder.append("Leaderboard for $quizType Quiz:\n\n")

        if (topScores.isEmpty()) {
            leaderboardBuilder.append("No scores available.")
        } else {
            for ((index, score) in topScores.withIndex()) {
                leaderboardBuilder.append("${index + 1}. ${score.first}: ${score.second}\n")
            }
        }

        leaderboardTextView.text = leaderboardBuilder.toString()
    }
}
