package com.example.surveyapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.surveyapplication.Model.*

class MainActivity : AppCompatActivity() {

    private val dbHelper: DatabaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun register(view: View) {
        val registerActivity = Intent(this, RegisterUser::class.java)
        startActivity(registerActivity)
    }

    fun signIn(view: View) {
        val username = findViewById<EditText>(R.id.usernameLogin).text.toString()
        val password = findViewById<EditText>(R.id.passwordLogin).text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in the required fields before signing in",
                            Toast.LENGTH_LONG).show()
        } else {
            val radioGroup = findViewById<RadioGroup>(R.id.radioGroupAS)
            val clearance = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)

            if (clearance.text.toString() == "I am student") {
                val result = dbHelper.retrieveStudent(Student(-1, username, password))

                if (result) {
                    Toast.makeText(this, "Sign in successful!", Toast.LENGTH_LONG).show()
                    val studentSignIn = Intent(this, StudentSignIn::class.java)
                    startActivity(studentSignIn)
                } else {
                    Toast.makeText(this, "Username or password incorrect...", Toast.LENGTH_LONG).show()
                }
            } else if (clearance.text.toString() == "I am admin") {
                val result = dbHelper.retrieveAdmin(Admin(-1, username, password))

                if (result) {
                    Toast.makeText(this, "Sign in successful!", Toast.LENGTH_LONG).show()
                    val adminSignIn = Intent(this, AdminSignIn::class.java)
                    startActivity(adminSignIn)
                } else {
                    Toast.makeText(this, "Username or password incorrect...", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please select student or admin!",
                                Toast.LENGTH_LONG).show()
            }
        }
    }
}