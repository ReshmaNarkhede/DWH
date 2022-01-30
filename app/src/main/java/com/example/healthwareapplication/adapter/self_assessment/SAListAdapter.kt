package com.example.healthwareapplication.adapter.self_assessment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.databinding.SaListItemBinding
import com.example.healthwareapplication.model.self_assessment.SAListModel
import org.json.JSONArray
import org.json.JSONObject

class SAListAdapter(val context: Context,dataArr: JSONArray, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<SAListAdapter.ViewHolder>() {

    private val dataArray: JSONArray = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SaListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataArray.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bindView(position,dataArray.getJSONObject(position), itemClickListener)
    }


    inner class ViewHolder(private val binding: SaListItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bindView(
            position: Int,
            jsonObject: JSONObject,
            clickListener: RecyclerItemClickListener.OnItemClickListener
        ) {
            val model = SAListModel(jsonObject)
            binding.dateTxt.text = model.getReportDate().plus("  ").plus(model.getReportTime())
            binding.symptomTxt.text = model.getSymptom()

            val info = AppSessions.getUserName(context) + ", " + AppSessions.getUserSex(context) + ", " + AppSessions.getUserAge(context)
            binding.userInfoTxt.text = info

            binding.root.setOnClickListener {
                clickListener.onItemClick(itemView,position)
            }
        }
    }
}