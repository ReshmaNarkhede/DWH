package com.example.healthwareapplication.adapter.self_assessment.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.databinding.RadioItemBinding

class RadioRecyclerViewAdapter(
    ansAry: List<String>,
    private val itemClickListener: RecyclerItemClickListener.OnItemClickListener
) : RecyclerView.Adapter<RadioRecyclerViewAdapter.ViewHolder>() {

    private val ansAry: List<String> = ansAry
    var selectedIndex = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RadioItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return ansAry.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bindView(position, ansAry, itemClickListener)
    }

    inner class ViewHolder(private val binding: RadioItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(
            position: Int, ansAry: List<String>,
            clickListener: RecyclerItemClickListener.OnItemClickListener
        ) {
            val answer = ansAry[position].toLowerCase().split(' ').joinToString(" ") { it.capitalize() }
            binding.textView.text = answer.replace("\\'", "'")

            binding.textView.setOnClickListener(View.OnClickListener {
                clickListener.onItemClick(itemView, position)
            })
        }
    }
}