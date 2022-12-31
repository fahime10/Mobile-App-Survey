package com.example.surveyapplication.Controller

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.surveyapplication.Model.*
import com.example.surveyapplication.R

class ViewQuestions : AppCompatActivity() {

    private lateinit var questions: QuestionList
    private lateinit var answers: ArrayList<Answer>
    private var studentSurveyAnswers: ArrayList<StudentSurveyAnswer> = ArrayList()
    private var studentId = 0
    private var surveyId = 0
    private val dbHelper: DatabaseHelper = DatabaseHelper(this)

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_questions)

        surveyId = intent.getIntExtra("surveyId", 0)
        studentId = intent.getIntExtra("studentId", 0)
        val surveyTitle = intent.getStringExtra("surveyTitle")

        findViewById<TextView>(R.id.chosenSurvey).text = surveyTitle

        questions = QuestionList(this, surveyId)

        findViewById<TextView>(R.id.question).text = questions.getQuestion(index).questionText

        answers = dbHelper.retrieveAllAnswers()

        findViewById<RadioButton>(R.id.a1).text = answers[0].answerText
        findViewById<RadioButton>(R.id.a1).isChecked = true
        findViewById<RadioButton>(R.id.a2).text = answers[1].answerText
        findViewById<RadioButton>(R.id.a3).text = answers[2].answerText
        findViewById<RadioButton>(R.id.a4).text = answers[3].answerText
        findViewById<RadioButton>(R.id.a5).text = answers[4].answerText
    }

    fun next(view: View) {
        val answerRadioGroup = findViewById<RadioGroup>(R.id.answerGroup)
        val answer = findViewById<RadioButton>(answerRadioGroup.checkedRadioButtonId)
        if (index < questions.getQuestionList().size) {
            if (index == questions.getQuestionList().size-2) {
                findViewById<Button>(R.id.next).text = "Finish"
            }

            val answerId = when (answer.text.toString()) {
                "Strongly agree" -> 1
                "Agree" -> 2
                "Neither agree nor disagree" -> 3
                "Disagree" -> 4
                "Strongly disagree" -> 5
                else -> 0
            }

            index++
            if (index != 10) {
                val surveyAnswer = StudentSurveyAnswer(-1, studentId, surveyId,
                    questions.getQuestionId(index), answerId)

                studentSurveyAnswers.add(surveyAnswer)

                findViewById<TextView>(R.id.question).text =
                    questions.getQuestion(index).questionText
            } else {
                Toast.makeText(this, "Thank you for completing the survey",
                    Toast.LENGTH_LONG).show()
                dbHelper.storeAllAnswers(studentSurveyAnswers)
                finish()
                val studentSignIn = Intent(this, StudentSignIn::class.java)
                studentSignIn.putExtra("studentId", studentId)
                startActivity(studentSignIn)
            }
        }
    }

    fun previous(view: View) {
        if (index - 1 >= 0) {
            studentSurveyAnswers.removeLast()
            index--
            if (index < questions.getQuestionList().size-1) {
                findViewById<Button>(R.id.next).text = "Next"
            }
            findViewById<TextView>(R.id.question).text =
                questions.getQuestion(index).questionText
        }
    }
}