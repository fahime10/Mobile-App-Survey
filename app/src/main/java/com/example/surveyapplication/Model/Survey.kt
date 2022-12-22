package com.example.surveyapplication.Model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Survey(val id: Int, val title: String, val startDate: String, val endDate: String) : Serializable {

    fun getSurveyStartDate(): Date {
        val dateStr = startDate
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.parse(dateStr)
    }

    fun getSurveyEndDate(): Date {
        val dateStr = endDate
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.parse(dateStr)
    }
}