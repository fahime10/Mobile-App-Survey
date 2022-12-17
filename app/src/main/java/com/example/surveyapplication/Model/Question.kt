package com.example.surveyapplication.Model

import java.io.Serializable

data class Question(val id: Int, val surveyId: Int, val questionText: String): Serializable {
}