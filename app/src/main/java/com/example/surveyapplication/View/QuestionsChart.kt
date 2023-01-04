package com.example.surveyapplication.View

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapplication.Controller.AdminSignIn
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.Question
import com.example.surveyapplication.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

class QuestionsChart : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var questionList: ArrayList<Question>
    private val dbHelper: DatabaseHelper = DatabaseHelper(this)
    private var title: String? = ""
    private var id: Int = 0
    private var index = 0

    private val entries: ArrayList<PieEntry> = ArrayList()

    private var answer1: Float = 0f
    private var answer2: Float = 0f
    private var answer3: Float = 0f
    private var answer4: Float = 0f
    private var answer5: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_chart)

        id = intent.getIntExtra("surveyId", 0)
        title = intent.getStringExtra("surveyTitle")
        findViewById<TextView>(R.id.surveyName).text = title

        questionList = dbHelper.retrieveAllQuestions(id)
        findViewById<TextView>(R.id.questionStat).text = questionList[index].questionText

        // on below line we are initializing our
        // variable with their ids.
        pieChart = findViewById(R.id.pieChart)

        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.rotationAngle = 0f

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        answer1 = dbHelper.surveyAnswers(questionList[index].id, 5).toFloat()
        answer2 = dbHelper.surveyAnswers(questionList[index].id, 4).toFloat()
        answer3 = dbHelper.surveyAnswers(questionList[index].id, 3).toFloat()
        answer4 = dbHelper.surveyAnswers(questionList[index].id, 2).toFloat()
        answer5 = dbHelper.surveyAnswers(questionList[index].id, 1).toFloat()

        entries.add(PieEntry(answer1))
        entries.add(PieEntry(answer2))
        entries.add(PieEntry(answer3))
        entries.add(PieEntry(answer4))
        entries.add(PieEntry(answer5))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Survey")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.limegreen))
        colors.add(resources.getColor(R.color.green))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.light_red))
        colors.add(resources.getColor(R.color.red))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }

    fun nextQuestionChart(view: View) {
        if (index < questionList.size) {
            if (index == questionList.size-2) {
                findViewById<Button>(R.id.backBtn).text = "Finish"
            }

            index++
            if (index != 10) {
                findViewById<TextView>(R.id.questionStat).text = questionList[index].questionText

                answer1 = dbHelper.surveyAnswers(questionList[index].id, 5).toFloat()
                answer2 = dbHelper.surveyAnswers(questionList[index].id, 4).toFloat()
                answer3 = dbHelper.surveyAnswers(questionList[index].id, 3).toFloat()
                answer4 = dbHelper.surveyAnswers(questionList[index].id, 2).toFloat()
                answer5 = dbHelper.surveyAnswers(questionList[index].id, 1).toFloat()

                entries[0] = PieEntry(answer1)
                entries[1] = PieEntry(answer2)
                entries[2] = PieEntry(answer3)
                entries[3] = PieEntry(answer4)
                entries[4] = PieEntry(answer5)

                val dataSet = PieDataSet(entries, "Survey")
                dataSet.sliceSpace = 3f
                dataSet.iconsOffset = MPPointF(0f, 40f)
                dataSet.selectionShift = 5f

                // add a lot of colors to list
                val colors: ArrayList<Int> = ArrayList()
                colors.add(resources.getColor(R.color.limegreen))
                colors.add(resources.getColor(R.color.green))
                colors.add(resources.getColor(R.color.yellow))
                colors.add(resources.getColor(R.color.light_red))
                colors.add(resources.getColor(R.color.red))

                // on below line we are setting colors.
                dataSet.colors = colors

                // on below line we are setting pie data set
                val data = PieData(dataSet)
                data.setValueFormatter(PercentFormatter())
                data.setValueTextSize(15f)
                data.setValueTypeface(Typeface.DEFAULT_BOLD)
                data.setValueTextColor(Color.WHITE)
                pieChart.data = data

                pieChart.highlightValues(null)
                pieChart.invalidate()
            } else {
                finish()
                val adminSignIn = Intent(this, AdminSignIn::class.java)
                startActivity(adminSignIn)
            }
        }
    }

    fun previousQuestionChart(view: View) {
        if (index > 0) {
            if (index < 9) {
                findViewById<TextView>(R.id.backBtn).text = "Next"
            }
            index--
            findViewById<TextView>(R.id.questionStat).text = questionList[index].questionText

            answer1 = dbHelper.surveyAnswers(questionList[index].id, 5).toFloat()
            answer2 = dbHelper.surveyAnswers(questionList[index].id, 4).toFloat()
            answer3 = dbHelper.surveyAnswers(questionList[index].id, 3).toFloat()
            answer4 = dbHelper.surveyAnswers(questionList[index].id, 2).toFloat()
            answer5 = dbHelper.surveyAnswers(questionList[index].id, 1).toFloat()

            entries[0] = PieEntry(answer1)
            entries[1] = PieEntry(answer2)
            entries[2] = PieEntry(answer3)
            entries[3] = PieEntry(answer4)
            entries[4] = PieEntry(answer5)

            val dataSet = PieDataSet(entries, "Survey")
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            val colors: ArrayList<Int> = ArrayList()
            colors.add(resources.getColor(R.color.limegreen))
            colors.add(resources.getColor(R.color.green))
            colors.add(resources.getColor(R.color.yellow))
            colors.add(resources.getColor(R.color.light_red))
            colors.add(resources.getColor(R.color.red))

            dataSet.colors = colors

            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)
            pieChart.data = data

            pieChart.highlightValues(null)
            pieChart.invalidate()
        }
    }
}