package com.example.surveyapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.surveyapplication.Model.CustomAdapter
import com.example.surveyapplication.Model.SurveyList

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
}