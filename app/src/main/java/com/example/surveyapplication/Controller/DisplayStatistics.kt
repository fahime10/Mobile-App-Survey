package com.example.surveyapplication.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.surveyapplication.Model.QuestionList
import com.example.surveyapplication.Model.StudentSurveyAnswerList
import com.example.surveyapplication.R
import com.example.surveyapplication.View.CustomAdapterStats

class DisplayStatistics : AppCompatActivity() {

    private var surveyId: Int = 0
    private var surveyTitle: String? = null
    private var participants: Int = 0
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_statistics)

        surveyId = intent.getIntExtra("surveyId", 0)
        surveyTitle = intent.getStringExtra("surveyTitle")
        participants = intent.getIntExtra("participants", 0)

        findViewById<TextView>(R.id.surveyTitleStat).text = surveyTitle

        val questionList = QuestionList(this, surveyId)

        val customAdapterStat = CustomAdapterStats(applicationContext, questionList, participants)

        listView = findViewById(R.id.statListview)

        listView.adapter = customAdapterStat

        findViewById<TextView>(R.id.countStudents).text = participants.toString()
    }

    fun displayCharts(view: View) {

    }
}