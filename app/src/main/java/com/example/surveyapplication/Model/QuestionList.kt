package com.example.surveyapplication.Model

import android.content.Context

class QuestionList(context: Context, surveyId: Int) {

    private var questionList: ArrayList<Question> = ArrayList()
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    init {
        questionList = dbHelper.retrieveAllQuestions(surveyId)
    }

    fun getCount(): Int {
        return questionList.size
    }

    fun getQuestionList(): ArrayList<Question> {
        return questionList
    }

    fun getQuestion(index: Int): Question {
        if (index !in 0..questionList.size)
        {
            throw ArrayIndexOutOfBoundsException("Index out of bounds")
        }
        return questionList[index]
    }

    fun getQuestionId(position: Int): Int {
        return questionList[position].id
    }
}