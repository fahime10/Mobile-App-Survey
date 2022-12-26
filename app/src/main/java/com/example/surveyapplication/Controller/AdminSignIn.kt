package com.example.surveyapplication.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.example.surveyapplication.MainActivity
import com.example.surveyapplication.Model.CustomAdapter
import com.example.surveyapplication.Model.CustomAdapterAdmin
import com.example.surveyapplication.Model.SurveyList
import com.example.surveyapplication.R

class AdminSignIn : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var surveyList: SurveyList
    private lateinit var surveyTitle: String
    private var surveyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_in)

        surveyList = SurveyList(this)

//        val customAdapter = CustomAdapter(applicationContext, surveyList)

        val customAdapterAdmin = CustomAdapterAdmin(applicationContext, surveyList)

        listView = findViewById(R.id.AdminListView)

        listView.adapter = customAdapterAdmin

        listView.setOnItemClickListener { parent, view, position, id ->
            surveyId = surveyList.getSurveyId(id.toInt())
            surveyTitle = surveyList.getSurveyTitle(id.toInt())
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

    fun signOut(view: View) {
        finish()
        val mainPage = Intent(this, MainActivity::class.java)
        startActivity(mainPage)
    }
}