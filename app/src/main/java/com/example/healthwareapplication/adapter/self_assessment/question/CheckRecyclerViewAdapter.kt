package com.example.healthwareapplication.adapter.self_assessment.question

import android.content.ClipData.Item
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.self_assessment.QuestionData
import kotlinx.android.synthetic.main.check_item.view.*
import org.json.JSONArray
import org.json.JSONObject


class CheckRecyclerViewAdapter(val ansArray: JSONArray?, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<CheckRecyclerViewAdapter.ViewHolder>() {
    val ansAry: JSONArray? = ansArray
    private var lastSelectedPosition = RecyclerView.NO_POSITION
    var itemStateArray = SparseBooleanArray()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.check_item, parent, false))
    }

    override fun getItemCount(): Int {
        return ansAry!!.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position,ansAry!!.getJSONObject(position),itemClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindView(position: Int, jsonObject: JSONObject,itemClickListener: RecyclerItemClickListener.OnItemClickListener) {
            val model = QuestionData.AnswerData(jsonObject)
            itemView.checked_text_view.text = model.getAnswerValue()

            itemView.checked_text_view.isChecked = model.getIsSelected()

            itemView.checked_text_view.setOnClickListener(View.OnClickListener {
//                val adapterPosition = adapterPosition
//                if (!itemStateArray[adapterPosition, false]) {
//                    itemView.checked_text_view.isChecked = true
//                    itemStateArray.put(adapterPosition, true)
//                } else {
//                    itemView.checked_text_view.isChecked = false
//                    itemStateArray.put(adapterPosition, false)
//                }
//                itemClickListener.onItemClick(itemView,position)
                if (lastSelectedPosition > 0) {
                    QuestionData.AnswerData(ansAry!!.getJSONObject(lastSelectedPosition)).setSelected(false)
                }

                if (model.getIsSelected()) {
                    jsonObject.put("is_selected", false)
                } else {
                    jsonObject.put("is_selected", true)
                }
                ansArray!!.put(position, jsonObject)
                lastSelectedPosition = adapterPosition
                notifyDataSetChanged()
                itemClickListener.onItemClick(itemView,position)
            })
        }
    }

    fun getSelected(): JSONArray? {
        val selected: JSONArray = JSONArray()
        for (i in 0 until ansArray!!.length()) {
            val model = QuestionData.AnswerData(ansArray.getJSONObject(i))
            if (model.getIsSelected()) {
                selected.put(ansArray.getJSONObject(i))
            }
        }
        return selected
    }
}