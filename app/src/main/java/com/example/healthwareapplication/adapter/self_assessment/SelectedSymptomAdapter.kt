package com.example.healthwareapplication.adapter.self_assessment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.activity.self_assessment.WhatFeelActivity
import com.example.healthwareapplication.databinding.SelectedSymptomListItemBinding
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import org.json.JSONArray
import org.json.JSONObject

class SelectedSymptomAdapter(
    val dataArr: JSONArray?,
    val showDeleted: WhatFeelActivity.ShowDeleted
) : RecyclerView.Adapter<SelectedSymptomAdapter.ViewHolder>() {

    val dataArray: JSONArray? = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectedSymptomListItemBinding.inflate(
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
        viewHolder.bindView(position, dataArray!!.getJSONObject(position), showDeleted)
    }


    inner class ViewHolder(private val binding: SelectedSymptomListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(
            position: Int,
            jsonObject: JSONObject,
            showDeleted: WhatFeelActivity.ShowDeleted
        ) {
            val model = SymptomJsonModel(jsonObject)
            binding.symptomName.text = model.getName()
            binding.cncleImg.setOnClickListener(View.OnClickListener {
                dataArr!!.remove(position)
                notifyDataSetChanged()
                showDeleted.showDeleted(dataArr.length());
            })
        }
    }
}