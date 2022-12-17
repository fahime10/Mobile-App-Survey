package com.example.surveyapplication.Model

data class StudentSurveyAnswer(val studentSurveyId: Int, val studentId: Int, val publishedSurveyId: Int,
                                val questionId: Int, val answerId: Int) {
}