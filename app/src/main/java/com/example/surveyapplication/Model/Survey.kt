package com.example.surveyapplication.Model

import java.io.Serializable

data class Survey(val id: Int, val title: String, val startDate: String, val endDate: String) : Serializable {
}