package com.example.surveyapplication.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.QuestionList
import com.example.surveyapplication.Model.StudentSurveyAnswerList
import com.example.surveyapplication.R

class CustomAdapterStats(private val appContext: Context,
                         private val questionList: QuestionList,
                            private val participants: Int) : BaseAdapter() {

    var counter = 0

    private var db: DatabaseHelper = DatabaseHelper(appContext)

    private val inflater: LayoutInflater =
        appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return questionList.getCount();
    }

    override fun getItem(i: Int): Any? {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        var view: View? = view
        view = inflater.inflate(R.layout.activity_statistics_view, parent, false)

        val questionCounter = view.findViewById<TextView>(R.id.statCounter)
        val saStat = view.findViewById<TextView>(R.id.SaStat)
        val aStat = view.findViewById<TextView>(R.id.AStat)
        val nStat = view.findViewById<TextView>(R.id.NStat)
        val dStat = view.findViewById<TextView>(R.id.DStat)
        val sdStat = view.findViewById<TextView>(R.id.SdStat)

        counter++
        questionCounter.text = counter.toString()
        saStat.text =
            (db.surveyAnswers(questionList.getQuestionList()[position].id, 1)/
                    participants * 100).toString().plus("%")

        aStat.text = (db.surveyAnswers(questionList.getQuestionList()[position].id, 2)/
                participants * 100).toString().plus("%")

        nStat.text = (db.surveyAnswers(questionList.getQuestionList()[position].id, 3)/
                participants * 100).toString().plus("%")

        dStat.text = (db.surveyAnswers(questionList.getQuestionList()[position].id, 4)/
                participants * 100).toString().plus("%")

        sdStat.text = (db.surveyAnswers(questionList.getQuestionList()[position].id, 5)/
                participants * 100).toString().plus("%")

        return view
    }
}