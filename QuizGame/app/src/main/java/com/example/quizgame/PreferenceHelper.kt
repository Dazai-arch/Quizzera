package com.example.quizgame

import android.content.Context

class PreferenceHelper(context: Context) {
    private val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)


    fun saveUserEmail(email: String) {
        preferences.edit().putString("USER_EMAIL", email).apply()
    }


    fun getUserEmail(): String? {
        return preferences.getString("USER_EMAIL", null)
    }


    fun clearUserEmail() {
        preferences.edit().remove("USER_EMAIL").apply()
    }
}




