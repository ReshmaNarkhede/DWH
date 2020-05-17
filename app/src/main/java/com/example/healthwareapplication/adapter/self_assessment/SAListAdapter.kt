package com.example.healthwareapplication.adapter.self_assessment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.self_assessment.SAListModel
import kotlinx.android.synthetic.main.sa_list_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class SAListAdapter(val context: Context,dataArr: JSONArray, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<SAListAdapter.ViewHolder>() {

    private val dataArray: JSONArray = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.sa_list_item,
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
            val model = SAListModel(jsonObject)

            itemView.dateTxt.text = model.getReportDate() + " " + model.getReportTime()
            itemView.symptomTxt.text = model.getSymptom()

            val info = AppSessions.getUserName(context) + ", " + AppSessions.getUserSex(context) + ", " + AppSessions.getUserAge(context)
            itemView.userInfoTxt.text = info

            itemView.setOnClickListener {
                clickListener.onItemClick(itemView,position)
            }
        }
    }
}