package com.example.healthwareapplication.adapter.self_assessment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.self_assessment.SymptomModel
import kotlinx.android.synthetic.main.symptom_list_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class SymptomAdapter(dataArr: JSONArray, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<SymptomAdapter.ViewHolder>() {

    private val dataArray: JSONArray = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.symptom_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataArray.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position,dataArray.getJSONObject(position), itemClickListener)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            position: Int,
            jsonObject: JSONObject,
            clickListener: RecyclerItemClickListener.OnItemClickListener
        ) {
            val model = SymptomModel(jsonObject)
            itemView.symptomName.text = model.getName()
            itemView.setOnClickListener {
                clickListener.onItemClick(itemView,position)
            }
        }
    }
}