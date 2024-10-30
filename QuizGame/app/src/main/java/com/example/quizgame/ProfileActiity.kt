package com.example.quizgame

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ProfileActiity : AppCompatActivity() {
    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var dbHelper: UserDatabaseHelper
    private var email: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_actiity)

        profileImageView = findViewById(R.id.profileImageView)
        usernameTextView = findViewById(R.id.usernameTextView)
        val changeImageButton = findViewById<Button>(R.id.changeImageButton)

        dbHelper = UserDatabaseHelper(this)


        email = intent.getStringExtra("USER_EMAIL")
        loadUserProfile(email)

        changeImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    private fun loadUserProfile(email: String?) {
        if (email != null) {
            val userImage = dbHelper.getUserImage(email)
            val username = dbHelper.getUsername(email)

            usernameTextView.text = username ?: "Unknown User"


            if (userImage != null) {
                profileImageView.setImageBitmap(BitmapFactory.decodeByteArray(userImage, 0, userImage.size))
            } else {
                Log.d("ProfileActivity", "No image found for user: $email")


            }
        } else {
            Log.d("ProfileActivity", "Email is null!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            profileImageView.setImageURI(imageUri)


            val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
            val byteArrayOutputStream = ByteArrayOutputStream()
            inputStream?.use { stream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead)
                }
            }
            val imageBytes = byteArrayOutputStream.toByteArray()

            // Log the size of the byte array
            Log.d("ProfileActivity", "Image byte array size: ${imageBytes.size}")


            if (email != null) {
                dbHelper.updateUserImage(email!!, imageBytes)
                Log.d("ProfileActivity", "Image updated for email: $email")
                loadUserProfile(email)
            } else {
                Log.d("ProfileActivity", "Email is null while updating image!")
            }
        }
    }
    override fun onResume() {
        super.onResume()

        val email = intent.getStringExtra("USER_EMAIL")
        val username = dbHelper.getUsername(email ?: "")
    }



}
