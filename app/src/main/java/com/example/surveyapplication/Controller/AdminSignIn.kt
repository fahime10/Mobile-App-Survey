package com.example.surveyapplication.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.example.surveyapplication.MainActivity
import com.example.surveyapplication.Model.CustomAdapter
import com.example.surveyapplication.Model.SurveyList
import com.example.surveyapplication.R

class AdminSignIn : AppCompatActivity() {

    lateinit var listView: ListView
    lateinit var surveyList: SurveyList
    private var surveyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sign_in)

        surveyList = SurveyList(this)

        val customAdapter = CustomAdapter(applicationContext, surveyList)

        listView = findViewById(R.id.AdminListView)

        listView.adapter = customAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            surveyId = surveyList.getSurveyId(id.toInt())
        }

    }

    fun createSurvey(view: View) {
        val createSurveyActivity = Intent(this, CreateSurvey::class.java)
        startActivity(createSurveyActivity)
    }

    fun updateSurvey(view: View) {

    }

    fun signOut(view: View) {
        finish()
        val mainPage = Intent(this, MainActivity::class.java)
        startActivity(mainPage)
    }
}