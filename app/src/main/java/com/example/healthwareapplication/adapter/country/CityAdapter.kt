package com.example.healthwareapplication.adapter.country

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.recycler_country_item
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.country.CityData
import kotlinx.android.synthetic.main.recycler_country_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class CityAdapter (val context: Context, dataArr: JSONArray, val searchStr:String?, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private val dataArray: JSONArray = dataArr

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                recycler_country_item,
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
            val obj = CityData(jsonObject)
            val originalText = obj.getName()
            val sb: Spannable = SpannableString(originalText)
            sb.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.pink)),
                originalText!!.toLowerCase().indexOf(searchStr!!.toLowerCase()),
                originalText!!.toLowerCase().indexOf(searchStr.toLowerCase()) + searchStr.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            itemView.name.text = sb
            itemView.setOnClickListener {
                clickListener.onItemClick(itemView,position)
            }
        }
    }
}