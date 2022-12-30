package com.example.surveyapplication.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.surveyapplication.MainActivity
import com.example.surveyapplication.View.CustomAdapterAdmin
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.SurveyList
import com.example.surveyapplication.R

class AdminSignIn : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var surveyList: SurveyList
    private lateinit var surveyTitle: String
    private var surveyId: Int = 0
    private var participants: Int = 0
    private val dbHelper: DatabaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_in)

        surveyList = SurveyList(this)

        val customAdapterAdmin = CustomAdapterAdmin(applicationContext, surveyList)

        listView = findViewById(R.id.AdminListView)

        listView.adapter = customAdapterAdmin


        listView.setOnItemClickListener { parent, view, position, id ->
            surveyId = surveyList.getSurveyId(id.toInt())
            surveyTitle = surveyList.getSurveyTitle(id.toInt())
            participants = dbHelper.listParticipants(surveyId)
        }
    }

    fun createSurvey(view: View) {
        val createSurveyActivity = Intent(this, CreateSurvey::class.java)
        startActivity(createSurveyActivity)
    }

    fun updateSurvey(view: View) {
        if (surveyId != 0) {
            val updateSurveyActivity = Intent(this, UpdateSurvey::class.java)
            updateSurveyActivity.putExtra("surveyId", surveyId)
            updateSurveyActivity.putExtra("surveyTitle", surveyTitle)
            startActivity(updateSurveyActivity)
        } else {
            Toast.makeText(this, "Please select a survey first!",
                            Toast.LENGTH_LONG).show()
        }
    }

    fun displayStatistics(view: View) {
        if (surveyId != 0) {
            val displayStats = Intent(this, DisplayStatistics::class.java)
            displayStats.putExtra("surveyId", surveyId)
            displayStats.putExtra("surveyTitle", surveyTitle)
            displayStats.putExtra("participants", participants)
            startActivity(displayStats)
        } else {
            Toast.makeText(this, "Please select a survey to display statistics...",
                            Toast.LENGTH_LONG).show()
        }
    }

    fun signOut(view: View) {
        finish()
        val mainPage = Intent(this, MainActivity::class.java)
        startActivity(mainPage)
    }
}