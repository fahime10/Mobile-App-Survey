package com.example.surveyapplication.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.example.surveyapplication.MainActivity
import com.example.surveyapplication.Model.*
import com.example.surveyapplication.R
import com.example.surveyapplication.View.CustomAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class StudentSignIn : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var surveyList: SurveyList
    private var surveyId: Int = 0
    private var surveyTitle: String = ""
    var studentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign_in)

        studentId = intent.getIntExtra("studentId", 0)

        surveyList = SurveyList(this)

        val date = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val str = date.format(dateFormatter)
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = formatter.parse(str)

        val surveysToRemove = ArrayList<Survey>()

        surveyList.getSurveyList().forEach { survey ->
            if (survey.getSurveyStartDate() > formattedDate ||
                survey.getSurveyEndDate() < formattedDate) {
                surveysToRemove.add(survey)
            }
        }

        val dbHelper = DatabaseHelper(this)

        val completedSurveys = dbHelper.checkCompletedSurveys(studentId)
        for (i in surveyList.getSurveyList()) {
            for (j in completedSurveys) {
                if (i.id == j) {
                    surveysToRemove.add(i)
                }
            }
        }

        surveyList.getSurveyList().removeAll(surveysToRemove)

        val customAdapter = CustomAdapter(applicationContext, surveyList)

        listView = findViewById(R.id.ListView)

        listView.adapter = customAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            surveyId = surveyList.getSurveyId(id.toInt())
            surveyTitle = surveyList.getSurveyTitle(id.toInt())
        }
    }


    fun selectSurvey(view: View) {
        if (surveyId != 0) {
            val startSurvey = Intent(this, ViewQuestions::class.java)
            startSurvey.putExtra("surveyId", surveyId)
            startSurvey.putExtra("studentId", studentId)
            startSurvey.putExtra("surveyTitle", surveyTitle)
            startActivity(startSurvey)
        } else {
            Toast.makeText(this, "Please select a survey first!",
                            Toast.LENGTH_LONG).show()
        }
    }

    fun signOut(view: View) {
        finish()
        val mainPage = Intent(this, MainActivity::class.java)
        startActivity(mainPage)
    }
}
