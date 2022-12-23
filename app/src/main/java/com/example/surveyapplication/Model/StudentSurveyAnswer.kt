package com.example.surveyapplication.Model

data class StudentSurveyAnswer(val studentSurveyAnswerId: Int, val studentId: Int,
                               val surveyId: Int, val questionId: Int, val answerId: Int) {
}