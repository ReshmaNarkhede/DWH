package com.example.healthwareapplication.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.recycler_home_menu_item
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.menu.MenuModel
import kotlinx.android.synthetic.main.recycler_home_menu_item.view.*

class HomeMenuAdapter(var mList: ArrayList<MenuModel>, private val itemClickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.Adapter<HomeMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(recycler_home_menu_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(mList[position],itemClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            obj: MenuModel,
            itemClickListener: RecyclerItemClickListener.OnItemClickListener
        ) {
            itemView.parentLayout.layoutParams.height = 540
            itemView.name.text = obj.menuName
            itemView.parentLayout.setBackgroundResource(obj.parant_bg_image)
            itemView.image.setBackgroundResource(obj.image)
            itemView.setOnClickListener {
                itemClickListener.onItemClick(itemView, position)
            }
        }
    }
}