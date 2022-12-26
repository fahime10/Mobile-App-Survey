package com.example.surveyapplication.Model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import com.example.surveyapplication.R
import org.w3c.dom.Text

class CustomAdapterAdmin(private val appContext: Context, private val surveyList: SurveyList) : BaseAdapter() {

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

        surveyTitle.text = surveyList.getSurvey(position).title
        surveyStartDate.text = surveyList.getSurvey(position).startDate
        surveyEndDate.text = surveyList.getSurvey(position).endDate

        return view
    }
}