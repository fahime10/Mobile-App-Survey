package com.example.surveyapplication.Controller

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.Question
import com.example.surveyapplication.Model.QuestionList
import com.example.surveyapplication.Model.Survey
import com.example.surveyapplication.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CreateSurvey : AppCompatActivity() {

    private var questionTextList: ArrayList<Question> = ArrayList<Question>()
    private lateinit var newSurvey: Survey
    private var newSurveyId: Int = 0
    private var startDateBtn: Button? = null
    private var textStartDate: TextView? = null
    private var endDateBtn: Button? = null
    private var textEndDate: TextView? = null
    private var cal = Calendar.getInstance()
    private var counter = 0
    private val dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_survey)

        textStartDate = findViewById<TextView>(R.id.startDateInput)
        textStartDate!!.text = "--/--/----"
        startDateBtn = findViewById(R.id.pickStartDate)

        textEndDate = findViewById<TextView>(R.id.endDateInput)
        textEndDate!!.text = "--/--/----"
        endDateBtn = findViewById(R.id.pickEndDate)

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

        startDateBtn!!.setOnClickListener {
            DatePickerDialog(
                this@CreateSurvey, startDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        endDateBtn!!.setOnClickListener {
            DatePickerDialog(
                this@CreateSurvey, endDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        findViewById<EditText>(R.id.questionWritten).isEnabled = false
        findViewById<Button>(R.id.previousBtn).isEnabled = false
        findViewById<Button>(R.id.nextBtn).isEnabled = false
        findViewById<Button>(R.id.changeSurveyDetails).isEnabled = false

        counter = findViewById<TextView>(R.id.counter).text.toString().toInt()
        newSurveyId = dbHelper.retrieveLastSurveyId() + 1
    }


    private fun updateStartDateInView() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        textStartDate!!.text = sdf.format(cal.time)
    }

    private fun updateEndDateInView() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        textEndDate!!.text = sdf.format(cal.time)
    }

    fun confirm(view: View) {
        val title = findViewById<EditText>(R.id.surveyTitleInput).text.toString()
        val startDate = findViewById<TextView>(R.id.startDateInput).text.toString()
        val endDate = findViewById<TextView>(R.id.endDateInput).text.toString()

        val formatter = SimpleDateFormat("dd/MM/yyyy")
        if (findViewById<TextView>(R.id.startDateInput).text.toString() != "--/--/----" &&
                findViewById<TextView>(R.id.endDateInput).text.toString() != "--/--/----") {
            val formattedStartDate = formatter.parse(startDate)
            val formattedEndDate = formatter.parse(endDate)

            if (title.isNotEmpty() && (formattedStartDate <= formattedEndDate)) {
                newSurvey = Survey(newSurveyId, title, startDate, endDate)
                findViewById<EditText>(R.id.surveyTitleInput).isEnabled = false
                findViewById<Button>(R.id.pickStartDate).isEnabled = false
                findViewById<Button>(R.id.pickEndDate).isEnabled = false
                findViewById<Button>(R.id.confirmBtn).isEnabled = false
                findViewById<Button>(R.id.changeSurveyDetails).isEnabled = true

                findViewById<EditText>(R.id.questionWritten).isEnabled = true
                findViewById<Button>(R.id.previousBtn).isEnabled = true
                findViewById<Button>(R.id.nextBtn).isEnabled = true
            } else {
                Toast.makeText(
                    this, "Please input all information before proceeding",
                    Toast.LENGTH_LONG
                ).show()
            }
            } else {
            Toast.makeText(this, "Please input all information before proceeding",
                            Toast.LENGTH_LONG).show()
            }
    }

    fun changeSurveyDetails(view: View) {
        findViewById<EditText>(R.id.surveyTitleInput).isEnabled = true
        findViewById<Button>(R.id.pickStartDate).isEnabled = true
        findViewById<Button>(R.id.pickEndDate).isEnabled = true
        findViewById<Button>(R.id.confirmBtn).isEnabled = true
        findViewById<Button>(R.id.changeSurveyDetails).isEnabled = false

        findViewById<EditText>(R.id.questionWritten).isEnabled = false
        findViewById<Button>(R.id.previousBtn).isEnabled = false
        findViewById<Button>(R.id.nextBtn).isEnabled = false
    }

    fun nextQuestion(view: View) {
        if (counter != 10) {
            val item = findViewById<TextView>(R.id.questionWritten).text.toString()
            if (item != "") {
                val question = Question(-1, newSurveyId, item)
                questionTextList.add(question)
                counter++
                findViewById<TextView>(R.id.counter).text = counter.toString()
                findViewById<EditText>(R.id.questionWritten).text.clear()
            } else {
                Toast.makeText(
                    this, "Question cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (questionTextList.size == 10) {
            dbHelper.addNewSurvey(newSurvey)

        }
    }

    fun previousQuestion(view: View) {

    }
}