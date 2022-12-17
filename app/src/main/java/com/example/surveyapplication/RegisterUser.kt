package com.example.surveyapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.Student

class RegisterUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
    }

    fun submitInfo(view: View) {
        val loginName = findViewById<EditText>(R.id.studentUsername).text.toString()
        val password = findViewById<EditText>(R.id.studentPassword).text.toString()

        if (password.length >= 8) {
            if (loginName.startsWith("p")) {

                val newStudent = Student(-1, loginName, password)
                val database = DatabaseHelper(this)
                val result = database.addStudent(newStudent)

                when (result) {
                    1 -> {
                        Toast.makeText(this, "You have been registered!", Toast.LENGTH_LONG).show()
                        val returnMain = Intent(this, MainActivity::class.java)
                        startActivity(returnMain)
                    }
                    -1 -> Toast.makeText(this, "Error on creating account...", Toast.LENGTH_LONG).show()
                    -2-> Toast.makeText(this, "Error! Cannot open database...", Toast.LENGTH_LONG).show()
                    -3 -> Toast.makeText(this, "Username already exists!", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(this,"Login name should start with 'p'!", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this,"Password should be 8 characters!", Toast.LENGTH_LONG).show()
        }

    }
}