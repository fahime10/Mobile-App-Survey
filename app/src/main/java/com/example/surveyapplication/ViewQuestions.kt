package com.example.surveyapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.surveyapplication.Model.Answer
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.Question
import com.example.surveyapplication.Model.QuestionList

class ViewQuestions : AppCompatActivity() {

    private lateinit var questions: QuestionList
    private lateinit var answers: ArrayList<Answer>
    private val dbHelper: DatabaseHelper = DatabaseHelper(this)
    private val answerRadioGroup = findViewById<RadioGroup>(R.id.answerGroup)

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questions)

        val surveyId = intent.getIntExtra("surveyId", 0)

        questions = QuestionList(this, surveyId)

        findViewById<TextView>(R.id.question).text = questions.getQuestion(index).questionText

        answers = dbHelper.retrieveAllAnswers()

        findViewById<RadioButton>(R.id.a1).text = answers[0].answerText
        findViewById<RadioButton>(R.id.a2).text = answers[1].answerText
        findViewById<RadioButton>(R.id.a3).text = answers[2].answerText
        findViewById<RadioButton>(R.id.a4).text = answers[3].answerText
        findViewById<RadioButton>(R.id.a5).text = answers[4].answerText
    }

    fun next(view: View) {
        val answer = findViewById<RadioButton>(answerRadioGroup.checkedRadioButtonId)
        if (index + 1 <= questions.getCount() && answer.isChecked) {
            if (index == questions.getCount()) {
                findViewById<Button>(R.id.next).text = "Finish"
            }
            index++
            findViewById<TextView>(R.id.question).text = questions.getQuestion(index).questionText
        } else if (!answer.isChecked) {
            Toast.makeText(this, "Please select an answer to proceed!", Toast.LENGTH_LONG).show()
        } else {
            val studentSignIn = Intent(this, StudentSignIn::class.java)
            startActivity(studentSignIn)
        }
    }

    fun previous(view: View) {
        if (index - 1 >= 0) {
            index--
            findViewById<TextView>(R.id.question).text = questions.getQuestion(index).questionText
        }
    }
}