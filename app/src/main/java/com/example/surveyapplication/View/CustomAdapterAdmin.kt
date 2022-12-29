package com.example.surveyapplication.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.surveyapplication.Model.DatabaseHelper
import com.example.surveyapplication.Model.SurveyList
import com.example.surveyapplication.R

class CustomAdapterAdmin(private val appContext: Context, private val surveyList: SurveyList) : BaseAdapter() {

    private val dbHelper: DatabaseHelper = DatabaseHelper(appContext)

    private val inflater: LayoutInflater =
        appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return surveyList.getCount();
    }

    override fun getItem(i: Int): Any? {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        var view: View? = view
        view = inflater.inflate(R.layout.activity_admin_view, parent, false)

        val surveyTitle = view.findViewById<TextView>(R.id.surveyAdmin)
        val surveyStartDate = view.findViewById<TextView>(R.id.startDateAdmin)
        val surveyEndDate = view.findViewById<TextView>(R.id.endDateAdmin)
        val participants = view.findViewById<TextView>(R.id.participants)

        surveyTitle.text = surveyList.getSurvey(position).title
        surveyStartDate.text = surveyList.getSurvey(position).startDate
        surveyEndDate.text = surveyList.getSurvey(position).endDate
        participants.text = dbHelper.listParticipants(surveyList.getSurvey(position).id).toString()

        return view
    }
}