package com.example.healthwareapplication.adapter.self_assessment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import kotlinx.android.synthetic.main.selected_symptom_list_item.view.*
import kotlinx.android.synthetic.main.symptom_list_item.view.symptomName
import org.json.JSONArray
import org.json.JSONObject

class SelectedSymptomAdapter(dataArr: JSONArray) : RecyclerView.Adapter<SelectedSymptomAdapter.ViewHolder>() {

    private val dataArray: JSONArray = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.selected_symptom_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataArray.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position,dataArray.getJSONObject(position))
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindView(position: Int, jsonObject: JSONObject) {
            val model = SymptomJsonModel(jsonObject)
            itemView.symptomName.text = model.getName()
            itemView.cncleImg.setOnClickListener(View.OnClickListener {
                dataArray.remove(position)
                notifyDataSetChanged()
            })
        }
    }
}