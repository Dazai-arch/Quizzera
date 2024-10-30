package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private var score = 0
    private var totalTime = 50 * 1000L // 50 seconds in milliseconds

    private lateinit var questionTextView: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var timerTextView: TextView
    private lateinit var questions: List<Question>
    private lateinit var questionImageView: ImageView


    private lateinit var quizType: String
    private lateinit var username: String
    private lateinit var userEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val email = intent.getStringExtra("USER_EMAIL")
        userEmail = email ?: "Unknown Email" // Store user email from Intent
        username = loadUsernameFromEmail(email)

        // Initialize UI elements
        questionTextView = findViewById(R.id.questionTextView)
        questionImageView = findViewById(R.id.questionImageView)
        optionButtons = listOf(
            findViewById(R.id.optionButton1),
            findViewById(R.id.optionButton2),
            findViewById(R.id.optionButton3),
            findViewById(R.id.optionButton4)
        )
        timerTextView = findViewById(R.id.timerTextView)

        // Get quiz type from intent
        quizType = intent.getStringExtra("quizType") ?: "capitals"

        // Load questions based on selected quiz type
        questions = when (quizType) {
            "capitals" -> loadCapitalsQuestions()
            "geography" -> loadGeographyQuestions()
            "discreteMath" -> loadDiscreteMathQuestions()
            "tech" -> loadTechQuestions()
            else -> emptyList()
        }


        startTimer()


        displayQuestion()


        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                checkAnswer(index)
            }
        }
    }

    private fun loadCapitalsQuestions(): List<Question> {
        return listOf(
            Question("What is the capital of France?", listOf("Paris", "Berlin", "Rome", "Madrid"), "Paris",R.drawable.france),
            Question("What is the capital of Germany?", listOf("Vienna", "Berlin", "Amsterdam", "Brussels"), "Berlin",R.drawable.germany),
            Question("What is the capital of Italy?", listOf("Athens", "Rome", "Lisbon", "Madrid"), "Rome",R.drawable.italy),
            Question("What is the capital of Spain?", listOf("Lisbon", "Barcelona", "Madrid", "Paris"), "Madrid",R.drawable.spain),
            Question("What is the capital of Japan?", listOf("Seoul", "Tokyo", "Kyoto", "Osaka"), "Tokyo",R.drawable.japan),
            Question("What is the capital of Canada?", listOf("Toronto", "Ottawa", "Vancouver", "Montreal"), "Ottawa",R.drawable.cannada),
            Question("What is the capital of Australia?", listOf("Sydney", "Melbourne", "Canberra", "Perth"), "Canberra",R.drawable.australia),
            Question("What is the capital of India?", listOf("Mumbai", "Kolkata", "New Delhi", "Bangalore"), "New Delhi",R.drawable.india),
            Question("What is the capital of Russia?", listOf("St. Petersburg", "Moscow", "Kiev", "Minsk"), "Moscow",R.drawable.russia),
            Question("What is the capital of Brazil?", listOf("Sao Paulo", "Rio de Janeiro", "Brasilia", "Salvador"), "Brasilia",R.drawable.brazil)

        )
    }

    private fun loadGeographyQuestions(): List<Question> {
        return listOf(
            Question("What is the largest desert in the world?", listOf("Sahara", "Arabian", "Gobi", "Kalahari"), "Sahara",R.drawable.desert),
            Question("Which river is the longest in the world?", listOf("Nile", "Amazon", "Yangtze", "Mississippi"), "Nile",R.drawable.river),
            Question("What is the tallest mountain in the world?", listOf("K2", "Kangchenjunga", "Everest", "Makalu"), "Everest",R.drawable.mountains),
            Question("Which continent is the Sahara Desert located on?", listOf("Asia", "Africa", "Australia", "South America"), "Africa",R.drawable.world),
            Question("What is the capital city of Australia?", listOf("Sydney", "Melbourne", "Canberra", "Brisbane"), "Canberra",R.drawable.australia),
            Question("Which ocean is the largest?", listOf("Atlantic", "Indian", "Arctic", "Pacific"), "Pacific",R.drawable.ocean),
            Question("What is the smallest country in the world?", listOf("Monaco", "Vatican City", "Nauru", "San Marino"), "Vatican City",R.drawable.world),
            Question("Which country has the largest population?", listOf("India", "USA", "China", "Indonesia"), "China",R.drawable.world),
            Question("What is the main language spoken in Brazil?", listOf("Spanish", "Portuguese", "French", "English"), "Portuguese",R.drawable.brazil),
            Question("Which mountain range separates Europe and Asia?", listOf("Andes", "Himalayas", "Rocky Mountains", "Ural Mountains"), "Ural Mountains",R.drawable.asia)

        )
    }

    private fun loadDiscreteMathQuestions(): List<Question> {
        return listOf(
            Question("What is the sum of angles in a triangle?", listOf("180 degrees", "360 degrees", "90 degrees", "270 degrees"), "180 degrees",R.drawable.triangle),
            Question("What is the term for a set that contains no elements?", listOf("Empty Set", "Universal Set", "Finite Set", "Infinite Set"), "Empty Set",R.drawable.discretemath),
            Question("Which of the following is a graph?", listOf("Circle", "Line", "Point", "All of the above"), "All of the above",R.drawable.chart),
            Question("What is the mode of the set {1, 2, 2, 3, 4}?", listOf("1", "2", "3", "4"), "2",R.drawable.discretemath),
            Question("What is the principle of mathematical induction?", listOf("Base case", "Inductive step", "Both A and B", "None of the above"), "Both A and B",R.drawable.discretemath),
            Question("What is the result of the logical expression true AND false?", listOf("True", "False", "Both true and false", "None of the above"), "False",R.drawable.logical),
            Question("How many edges does a cube have?", listOf("6", "8", "12", "10"), "12",R.drawable.cube),
            Question("Which of the following is a prime number?", listOf("4", "6", "7", "9"), "7",R.drawable.numberblocks),
            Question("What is the cardinality of the set {2, 4, 6}?", listOf("2", "3", "4", "5"), "3",R.drawable.numberblocks),
            Question("Which of the following is a function?", listOf("y = x^2", "y^2 = x", "y = 1/x", "All of the above"), "All of the above",R.drawable.numberblocks)

        )
    }

    private fun loadTechQuestions(): List<Question> {
        return listOf(
            Question("What does HTML stand for?", listOf("Hyper Text Markup Language", "High Text Markup Language", "Hyperlinks and Text Markup Language", "None of the above"), "Hyper Text Markup Language",R.drawable.html),
            Question("Which is not a programming language?", listOf("Python", "HTML", "Java", "C++"), "HTML",R.drawable.programming),
            Question("What does CSS stand for?", listOf("Cascading Style Sheets", "Colorful Style Sheets", "Creative Style Sheets", "None of the above"), "Cascading Style Sheets",R.drawable.css),
            Question("Which company developed the Android operating system?", listOf("Apple", "Google", "Microsoft", "IBM"), "Google",R.drawable.android),
            Question("What does 'URL' stand for?", listOf("Uniform Resource Locator", "Universal Resource Locator", "Uniform Resource Link", "None of the above"), "Uniform Resource Locator",R.drawable.url),
            Question("What is the primary function of the CPU?", listOf("Storage", "Processing", "Input", "Output"), "Processing",R.drawable.cpu),
            Question("Which protocol is used for sending emails?", listOf("HTTP", "FTP", "SMTP", "POP3"), "SMTP",R.drawable.protocol),
            Question("What does 'GUI' stand for?", listOf("Graphical User Interface", "General User Interface", "Graphical Uniform Interface", "General Uniform Interface"), "Graphical User Interface",R.drawable.ui),
            Question("What is the latest version of the Android operating system?", listOf("Android 13", "Android 14", "Android 12", "Android 15"), "Android 15",R.drawable.android),
            Question("What is the main purpose of an operating system?", listOf("Manage hardware resources", "Run applications", "Provide security", "All of the above"), "All of the above",R.drawable.os)

        )
    }

    private fun displayQuestion() {
        if (currentQuestionIndex < questions.size) {
            val currentQuestion = questions[currentQuestionIndex]

            // Display question and options
            questionTextView.text = currentQuestion.question
            optionButtons.forEachIndexed { index, button ->
                button.text = currentQuestion.options[index]
            }

            questionImageView.setImageResource(currentQuestion.imageResId)
        } else {

            endQuiz()
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val currentQuestion = questions[currentQuestionIndex]
        val selectedAnswer = currentQuestion.options[selectedIndex]

        if (selectedAnswer == currentQuestion.correctAnswer) {
            score++
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong! Correct answer is: ${currentQuestion.correctAnswer}", Toast.LENGTH_LONG).show()
        }


        currentQuestionIndex++
        displayQuestion()
    }

    private fun startTimer() {
        object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "Time left: $secondsLeft sec"
            }

            override fun onFinish() {
                // Time is up, end the quiz
                endQuiz()
            }
        }.start()
    }

    private fun endQuiz() {
        // Show final score
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("totalQuestions", questions.size)
        intent.putExtra("quizType", quizType)
        intent.putExtra("username", username)
        intent.putExtra("USER_EMAIL", userEmail)

        saveScoreToLeaderboard(username, score, quizType)

        startActivity(intent)
        finish()
    }


    private fun saveScoreToLeaderboard(username: String, score: Int, quizType: String) {
        val sharedPreferences = getSharedPreferences("QuizLeaderboard", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("$quizType:$username", "$score") // Format as "quizType:username"
        editor.apply()
    }


    private fun loadUsernameFromEmail(email: String?): String {
        val dbHelper = UserDatabaseHelper(this)
        return if (email != null) {
            dbHelper.getUsername(email) ?: "Unknown User" // Fetch username from database
        } else {
            "Unknown User"
        }
    }
}
