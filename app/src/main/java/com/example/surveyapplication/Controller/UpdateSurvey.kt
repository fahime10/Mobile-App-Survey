package com.example.surveyapplication.Controller

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.Question
import com.example.surveyapplication.Model.QuestionList
import com.example.surveyapplication.Model.Survey
import com.example.surveyapplication.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpdateSurvey : AppCompatActivity() {

    private var questionList = ArrayList<Question>()
    private lateinit var oldQuestions: QuestionList
    private var updateStartDateBtn: Button? = null
    private var updateStartDate: TextView? = null
    private var updateEndDateBtn: Button? = null
    private var updateEndDate: TextView? = null
    private var cal = Calendar.getInstance()
    private var questionCounter = 0
    private var surveyId: Int = 0
    private var surveyTitle: String? = null
    private lateinit var updatedSurvey: Survey
    private val dbHelper = DatabaseHelper(this)

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_survey)

        surveyId = intent.getIntExtra("surveyId", 0)
        surveyTitle = intent.getStringExtra("surveyTitle")

        findViewById<TextView>(R.id.oldSurvey).text = surveyTitle

        oldQuestions = QuestionList(this, surveyId)
        findViewById<TextView>(R.id.questionText).text =
            oldQuestions.getQuestion(index).questionText

        updateStartDate = findViewById<TextView>(R.id.updateStartDate)
        updateStartDateBtn = findViewById(R.id.updateStartDateBtn)

        updateEndDate = findViewById<TextView>(R.id.updateEndDate)
        updateEndDateBtn = findViewById(R.id.updateEndDateBtn)

        val startDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                updateStartDateInView()
            }

        val endDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                updateEndDateInView()
            }

        updateStartDateBtn!!.setOnClickListener {
            DatePickerDialog(
                this@UpdateSurvey, startDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        updateEndDateBtn!!.setOnClickListener {
            DatePickerDialog(
                this@UpdateSurvey, endDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        findViewById<EditText>(R.id.newQuestion).isEnabled = false
        findViewById<Button>(R.id.previousQuestion).isEnabled = false
        findViewById<Button>(R.id.nextQuestion).isEnabled = false
        findViewById<Button>(R.id.changeDetails).isEnabled = false

        questionCounter = findViewById<TextView>(R.id.questionCounter).text.toString().toInt()
    }

    private fun updateStartDateInView() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        updateStartDate!!.text = sdf.format(cal.time)
    }

    private fun updateEndDateInView() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        updateEndDate!!.text = sdf.format(cal.time)
    }

    fun confirmChoice(view: View) {
        val title = findViewById<EditText>(R.id.updateTitle).text.toString()
        val startDate = findViewById<TextView>(R.id.updateStartDate).text.toString()
        val endDate = findViewById<TextView>(R.id.updateEndDate).text.toString()

        val formatter = SimpleDateFormat("dd/MM/yyyy")
        if (findViewById<TextView>(R.id.updateStartDate).text.toString() != "--/--/----" &&
            findViewById<TextView>(R.id.updateEndDate).text.toString() != "--/--/----") {
            val formattedStartDate = formatter.parse(startDate)
            val formattedEndDate = formatter.parse(endDate)

            if (title.isNotEmpty() && (formattedStartDate <= formattedEndDate)) {
                updatedSurvey = Survey(surveyId, title, startDate, endDate)
                findViewById<EditText>(R.id.updateTitle).isEnabled = false
                findViewById<Button>(R.id.updateStartDateBtn).isEnabled = false
                findViewById<Button>(R.id.updateEndDateBtn).isEnabled = false
                findViewById<Button>(R.id.confirmSurvey).isEnabled = false
                findViewById<Button>(R.id.changeDetails).isEnabled = true

                findViewById<EditText>(R.id.newQuestion).isEnabled = true
                findViewById<Button>(R.id.previousQuestion).isEnabled = true
                findViewById<Button>(R.id.nextQuestion).isEnabled = true
            } else {
                Toast.makeText(
                    this, "Please input all correct information before proceeding",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(this, "Please input all correct information before proceeding",
                Toast.LENGTH_LONG).show()
        }
    }

    fun changeAboveDetails(view: View) {
            findViewById<EditText>(R.id.updateTitle).isEnabled = true
            findViewById<Button>(R.id.updateStartDateBtn).isEnabled = true
            findViewById<Button>(R.id.updateEndDateBtn).isEnabled = true
            findViewById<Button>(R.id.confirmSurvey).isEnabled = true
            findViewById<Button>(R.id.changeDetails).isEnabled = false

            findViewById<EditText>(R.id.newQuestion).isEnabled = false
            findViewById<Button>(R.id.previousQuestion).isEnabled = false
            findViewById<Button>(R.id.nextQuestion).isEnabled = false
    }

    fun nextQuestion(view: View) {
        if (questionCounter <= 10) {
            val item = findViewById<TextView>(R.id.newQuestion).text.toString()
            if (item != "") {
                val questionId = oldQuestions.getQuestionId(index)
                val question = Question(questionId, surveyId, item)
                questionList.add(question)
                questionCounter++

                if (index != 9) {
                    index++
                }
                findViewById<TextView>(R.id.questionCounter).text = questionCounter.toString()
                findViewById<EditText>(R.id.newQuestion).text.clear()

                if (questionCounter != 11) {
                    findViewById<TextView>(R.id.questionText).text =
                        oldQuestions.getQuestion(index).questionText
                }
            } else {
                Toast.makeText(this, "Question cannot be blank", Toast.LENGTH_LONG)
                    .show()
            }
        }

        if (questionList.size == 10) {
            dbHelper.updateSurvey(updatedSurvey)
            dbHelper.updateQuestions(questionList)
            finish()
            Toast.makeText(this, "Survey updated successfully!",
                            Toast.LENGTH_LONG).show()
            val adminView = Intent(this, AdminSignIn::class.java)
            startActivity(adminView)
        }
    }

    fun previousQuestion(view: View) {
        if (questionCounter != 1) {
            questionList.removeLast()

            if (index > 0) {
                index--
            }
            findViewById<TextView>(R.id.questionText).text = oldQuestions.getQuestion(index).questionText

            questionCounter--
            findViewById<TextView>(R.id.questionCounter).text = questionCounter.toString()
        }
    }
}