package com.example.quizgame

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 3 
        private const val DATABASE_NAME = "users.db"
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_IMAGE = "profile_image"
        private const val TABLE_LEADERBOARD = "leaderboard"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PASSWORD TEXT," +
                "$COLUMN_IMAGE BLOB)")
        db?.execSQL(createUserTable)

        val createScoresTable = ("CREATE TABLE scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT," +
                "quiz_type TEXT," +
                "score INTEGER)")
        db?.execSQL(createScoresTable)

        val createLeaderboardTable = ("CREATE TABLE $TABLE_LEADERBOARD (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "score INTEGER," +
                "quizType TEXT)")
        db?.execSQL(createLeaderboardTable)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS scores")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LEADERBOARD")
        onCreate(db)
    }

    fun addUser(
        username: String,
        email: String,
        password: String,
        image: ByteArray? = null
    ): Boolean {
        if (checkUser(email, password)) {
            return false // Return false if the email is already registered
        }

        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_IMAGE, image)

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != (-1).toLong()
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    @SuppressLint("Range")
    fun getUserImage(email: String): ByteArray? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_IMAGE FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )
        return if (cursor.moveToFirst()) {
            val imageBlob = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
            cursor.close()
            db.close()
            imageBlob
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Function to add score to the scores table
    fun addScore(email: String, quizType: String, score: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("email", email)
        values.put("quiz_type", quizType)
        values.put("score", score)

        val result = db.insert("scores", null, values)
        db.close()
        return result != (-1).toLong()
    }


    fun updateUserImage(email: String, image: ByteArray?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_IMAGE, image)

        val result = db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
        db.close()
        return result > 0
    }

    @SuppressLint("Range")
    fun getUsername(email: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_USERNAME FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )
        return if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
            cursor.close()
            db.close()
            username
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    @SuppressLint("Range")
    fun getTopScores(quizType: String): List<Pair<String, Int>> {
        val scores = mutableListOf<Pair<String, Int>>()
        val db = readableDatabase


        val cursor = db.rawQuery(
            "SELECT username, score FROM $TABLE_LEADERBOARD WHERE quizType = ? ORDER BY score DESC LIMIT 10",
            arrayOf(quizType)
        )

        if (cursor.moveToFirst()) {
            do {
                val username = cursor.getString(cursor.getColumnIndex("username"))
                val score = cursor.getInt(cursor.getColumnIndex("score"))
                scores.add(Pair(username, score))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return scores
    }

    fun addLeaderboardScore(username: String, score: Int, quizType: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("score", score)
        values.put("quizType", quizType)

        val result = db.insert(TABLE_LEADERBOARD, null, values)
        db.close()
        return result != (-1).toLong()
    }


}
