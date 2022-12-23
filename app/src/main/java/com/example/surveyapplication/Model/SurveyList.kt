package com.example.surveyapplication.Model

import android.content.Context
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class SurveyList(context: Context) : Serializable {

    private var surveyList: ArrayList<Survey> = ArrayList()
    private var dbHelper: DatabaseHelper = DatabaseHelper(context)

    init {
        surveyList = dbHelper.retrieveAllSurveys()
    }

    fun getCount(): Int {
        return surveyList.size
    }

    fun getSurveyList(): ArrayList<Survey> {
        return surveyList
    }

    fun getSurvey(index: Int): Survey {
        if (index !in 0..surveyList.size)
        {
            throw ArrayIndexOutOfBoundsException("Index out of bounds")
        }
        return surveyList[index]
    }

    fun removeSurvey(survey: Survey) {
        surveyList.remove(survey)
    }

    fun getSurveyId(position: Int): Int {
        return surveyList[position].id
    }
}