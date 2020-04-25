package com.example.healthwareapplication.adapter.self_assessment.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.radio_item.view.*

class RadioRecyclerViewAdapter(ansAry: List<String>, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<RadioRecyclerViewAdapter.ViewHolder>() {

    private val ansAry: List<String> = ansAry!!
    var selectedIndex = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.radio_item, parent, false))
    }

    override fun getItemCount(): Int {
        return ansAry.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bindView(position, ansAry,itemClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindView(
            position: Int, ansAry: List<String>,
            clickListener: RecyclerItemClickListener.OnItemClickListener) {
            itemView.radioBtn.text = ansAry[position]

            itemView.radioBtn.isChecked = position == selectedIndex

            itemView.radioBtn.setOnClickListener(View.OnClickListener {
                selectedIndex = adapterPosition
                clickListener.onItemClick(itemView,position)
                notifyDataSetChanged()
//                Log.e("radio button click: " ," " + model.getAnswerValue())
            })
        }
    }
}