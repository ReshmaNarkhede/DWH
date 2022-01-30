package com.example.healthwareapplication.adapter.self_assessment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.databinding.AssessmentListItemBinding
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.example.healthwareapplication.model.self_assessment.ReportData
import org.json.JSONArray
import org.json.JSONObject

class AssessmentAdapter(val dataArr: JSONArray?, val isHome: Boolean) :
    RecyclerView.Adapter<AssessmentAdapter.ViewHolder>() {

    val dataArray: JSONArray? = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AssessmentListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataArray!!.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position, dataArray!!.getJSONObject(position))
    }


    inner class ViewHolder(private val binding: AssessmentListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(
            position: Int,
            jsonObject: JSONObject
        ) {
            if (isHome) {
                val model = ReportData(jsonObject)
                binding.questionTxt.text = model.getQuestion()
                binding.answerTxt.text = model.getAnswer()
            } else {
                val model = QuestionData.QuestionAnsModel(jsonObject)
                binding.questionTxt.text = model.getQuestion()
                binding.answerTxt.text = model.getSelectedAnswer()
            }
        }
    }
}