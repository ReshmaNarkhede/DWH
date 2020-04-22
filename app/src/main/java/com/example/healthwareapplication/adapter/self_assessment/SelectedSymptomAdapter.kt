package com.example.healthwareapplication.adapter.self_assessment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.self_assessment.SADetailActivity
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import kotlinx.android.synthetic.main.selected_symptom_list_item.view.*
import kotlinx.android.synthetic.main.symptom_list_item.view.symptomName
import org.json.JSONArray
import org.json.JSONObject

class SelectedSymptomAdapter(
    val dataArr: JSONArray?,
    val showDeleted: SADetailActivity.ShowDeleted
) : RecyclerView.Adapter<SelectedSymptomAdapter.ViewHolder>() {

    val dataArray: JSONArray? = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.selected_symptom_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataArray!!.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position, dataArray!!.getJSONObject(position), showDeleted)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(
            position: Int,
            jsonObject: JSONObject,
            showDeleted: SADetailActivity.ShowDeleted
        ) {
            val model = SymptomJsonModel(jsonObject)
            itemView.symptomName.text = model.getName()
            itemView.cncleImg.setOnClickListener(View.OnClickListener {
                dataArr!!.remove(position)
                notifyDataSetChanged()
                showDeleted.showDeleted(dataArr.length());
            })
        }
    }
}