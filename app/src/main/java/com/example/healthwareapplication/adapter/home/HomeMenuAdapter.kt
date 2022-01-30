package com.example.healthwareapplication.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.databinding.RecyclerHomeMenuItemBinding
import com.example.healthwareapplication.model.menu.MenuModel

class HomeMenuAdapter(var mList: ArrayList<MenuModel>, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<HomeMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerHomeMenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(mList[position],itemClickListener)
    }

    inner class ViewHolder(private val binding: RecyclerHomeMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(
            obj: MenuModel,
            itemClickListener: RecyclerItemClickListener.OnItemClickListener
        ) {
            binding.parentLayout.layoutParams.height = 540
            binding.name.text = obj.menuName
            binding.parentLayout.setBackgroundResource(obj.parant_bg_image)
            binding.image.setBackgroundResource(obj.image)
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(itemView, position)
            }
        }
    }
}