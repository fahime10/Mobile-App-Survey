package com.example.surveyapplication.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.example.surveyapplication.MainActivity
import com.example.surveyapplication.Model.*
import com.example.surveyapplication.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class StudentSignIn : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var surveyList: SurveyList
    private var surveyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign_in)

        surveyList = SurveyList(this)

        val customAdapter = CustomAdapter(applicationContext, surveyList)

        listView = findViewById(R.id.ListView)

        listView.adapter = customAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            surveyId = surveyList.getSurveyId(id.toInt())
        }

    }

    fun selectSurvey(view: View) {
        var idPosition: Int = 0

        for (i in 0 until surveyList.getCount()) {
            if (surveyList.getSurveyId(i) == surveyId) {
                idPosition = i
            }
        }

        val date = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val str = date.format(dateFormatter)
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = formatter.parse(str)
        val startDate = surveyList.getSurveyStartDate(idPosition)
        val endDate = surveyList.getSurveyEndDate(idPosition)
        if (formattedDate > startDate && endDate > formattedDate) {
            val startSurvey = Intent(this, ViewQuestions::class.java)
            startSurvey.putExtra("surveyId", surveyId)
            startActivity(startSurvey)
        }
    }

    fun signOut(view: View) {
        finish()
        val mainPage = Intent(this, MainActivity::class.java)
        startActivity(mainPage)
    }
}
