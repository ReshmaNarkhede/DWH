package com.example.healthwareapplication.adapter.self_assessment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.model.self_assessment.QuestionData
import kotlinx.android.synthetic.main.assessment_list_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class AssessmentAdapter(val dataArr: JSONArray?) : RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    val dataArray: JSONArray? = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.assessment_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataArray!!.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position, dataArray!!.getJSONObject(position))
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            position: Int,
            jsonObject: JSONObject
        ) {
            val model = QuestionData.QuestionAnsModel(jsonObject)
            itemView.questionTxt.text = model.getQuestion()
            itemView.answerTxt.text = model.getSelectedAnswer()
        }
    }
}