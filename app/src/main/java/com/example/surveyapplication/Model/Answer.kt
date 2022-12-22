package com.example.surveyapplication.Model

data class Answer(val id: Int, val answerText: String) {

    fun getAnswerId(): Int {
        return id
    }
}