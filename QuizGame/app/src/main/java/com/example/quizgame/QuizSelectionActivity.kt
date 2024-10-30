package com.example.quizgame

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class QuizSelectionActivity : AppCompatActivity() {
    private lateinit var preferenceHelper: PreferenceHelper

    private lateinit var leaderboardImageView: ImageView
    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_selection)

        dbHelper = UserDatabaseHelper(this)
        preferenceHelper = PreferenceHelper(this)

        val capitalsButton = findViewById<Button>(R.id.capitalsQuizButton)
        val techButton = findViewById<Button>(R.id.techQuizButton)
        val geographyButton = findViewById<Button>(R.id.geographyQuizButton)
        val discreteMathButton = findViewById<Button>(R.id.discreteMathQuizButton)
        val profileButton = findViewById<ImageView>(R.id.imageView)
        leaderboardImageView = findViewById(R.id.leaderboardImageView)


        val email = intent.getStringExtra("USER_EMAIL")
        val username = dbHelper.getUsername(email ?: "")


        Glide.with(this)
            .asGif()
            .load(R.raw.podiumbackfree)
            .into(leaderboardImageView)

        capitalsButton.setOnClickListener {
            startQuizActivity("capitals", email, username)
        }

        techButton.setOnClickListener {
            startQuizActivity("tech", email, username)
        }

        geographyButton.setOnClickListener {
            startQuizActivity("geography", email, username)
        }

        discreteMathButton.setOnClickListener {
            startQuizActivity("discreteMath", email, username)
        }

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActiity::class.java)
            intent.putExtra("USERNAME", username)
            intent.putExtra("USER_EMAIL", email)
            startActivity(intent)
        }




        val logoutButton = findViewById<Button>(R.id.logout)
        logoutButton.setOnClickListener {
            // Clear the user's email from SharedPreferences
            preferenceHelper.clearUserEmail()

            // Redirect to the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close the QuizSelectionActivity so the user can't go back to it

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        }
        if (preferenceHelper.getUserEmail() == null) {
            // Redirect to LoginActivity if not logged in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close this activity
            return // Exit onCreate
        }


        leaderboardImageView.setOnClickListener {
            val quizTypes = arrayOf("capitals", "tech", "geography", "discreteMath")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose Quiz Type")
            builder.setItems(quizTypes) { _, which ->
                val intent = Intent(this, LeaderBoardActivity::class.java)
                intent.putExtra("quizType", quizTypes[which])
                startActivity(intent)
            }
            builder.show()
        }
    }

    private fun startQuizActivity(quizType: String, email: String?, username: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("quizType", quizType)
        intent.putExtra("USERNAME", username)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
    }


    data class Quiz(
        val quizId: Int,
        val quizName: String
    )

}
