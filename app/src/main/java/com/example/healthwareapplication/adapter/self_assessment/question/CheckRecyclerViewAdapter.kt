package com.example.healthwareapplication.adapter.self_assessment.question

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.containsValue
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.check_item.view.*


class CheckRecyclerViewAdapter(val ansArray: List<String>, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<CheckRecyclerViewAdapter.ViewHolder>() {
    private val ansAry: List<String> = ansArray!!
    private var lastSelectedPosition = RecyclerView.NO_POSITION
    private val storeChecked = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.check_item, parent, false))
    }

    override fun getItemCount(): Int {
        return ansAry!!.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(position,ansAry,itemClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindView(position: Int, jsonObject: List<String>, itemClickListener: RecyclerItemClickListener.OnItemClickListener) {
            val model = ansArray[position]
            itemView.checked_text_view.text = ansArray[position]

//            itemView.checked_text_view.isChecked = model.getIsSelected()

            itemView.checked_text_view.setOnClickListener(View.OnClickListener {
//                if (lastSelectedPosition > 0) {
//                    QuestionData.AnswerData(ansAry!!.getJSONObject(lastSelectedPosition)).setSelected(false)
//                }
//
//                if (model.getIsSelected()) {
//                    jsonObject.put("is_selected", false)
//                } else {
//                    jsonObject.put("is_selected", true)
//                }
//                ansArray!!.put(position, jsonObject)
//                lastSelectedPosition = adapterPosition
//                notifyDataSetChanged()
                val adapterPosition = adapterPosition
                if (!storeChecked[adapterPosition, false]) {
                    itemView.checked_text_view.isChecked = true
                    storeChecked.put(adapterPosition, true)
                } else {
                    itemView.checked_text_view.isChecked = false
                    storeChecked.put(adapterPosition, false)
                }
                itemClickListener.onItemClick(itemView,position)
            })
        }
    }

//    fun getSelected(): JSONArray? {
//        val selected: JSONArray = JSONArray()
//        for (i in 0 until ansArray!!.length()) {
//            val model = QuestionData.AnswerData(ansArray.getJSONObject(i))
//            if (model.getIsSelected()) {
//                selected.put(ansArray.getJSONObject(i))
//            }
//        }
//        return selected
//    }

    fun getSelectedItems(): List<String>? {
        val items: MutableList<String> = ArrayList(storeChecked.size())
        for (i in 0 until storeChecked.size()) {
            val key = storeChecked.keyAt(i)
            if(storeChecked.get(key)) {
                Log.e("check size: ", ": $key")
                items.add(ansAry[key])
            }
        }
        return items
    }
}