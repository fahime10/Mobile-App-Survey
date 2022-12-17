package com.example.surveyapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import com.example.surveyapplication.Model.*


class StudentSignIn : AppCompatActivity() {

    lateinit var listView: ListView
    private var surveyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign_in)

        val surveyList = SurveyList(this)

        val customAdapter = CustomAdapter(applicationContext, surveyList)

        listView = findViewById(R.id.ListView)

        listView.adapter = customAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            surveyId = surveyList.getSurvey(id.toInt()).id
        }

    }

    fun selectSurvey(view: View) {
        val startSurvey = Intent(this, ViewQuestions::class.java)
        startSurvey.putExtra("surveyId", surveyId)
        startActivity(startSurvey)
    }

    fun signOut(view: View) {
        val mainPage = Intent(this, MainActivity::class.java)
        startActivity(mainPage)
    }
}
