package com.example.healthwareapplication.adapter.country

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.country.CountryData
import kotlinx.android.synthetic.main.recycler_country_item.view.*
import org.json.JSONArray
import org.json.JSONObject


class CountryAdapter(
    private var dataArr: JSONArray,
private val itemClickListener: RecyclerItemClickListener.OnItemClickListener
) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {
    var mFilter: ItemFilter = ItemFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_country_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataArr.length()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bindView(dataArr.getJSONObject(position), itemClickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            jsonObject: JSONObject,
            itemClickListener: RecyclerItemClickListener.OnItemClickListener
        ) {

            val obj = CountryData(jsonObject)

            Glide.with(itemView.context).load(obj.getImage())
                .apply(RequestOptions().placeholder(R.drawable.dwh_logo)).into(itemView.image)
            itemView.name.text = obj.getName()

            itemView.setOnClickListener {
                itemClickListener.onItemClick(itemView, position)
            }
        }
    }

    fun getFilter(): Filter {
        return mFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {

            val filterString = constraint.toString().toLowerCase()

            val results = Filter.FilterResults()

            val list = dataArr

            val count = list.length()
            val nlist = JSONArray()

            var filterableString: String

            for (i in 0 until count) {
                val countryData = CountryData(list.getJSONObject(i))
                filterableString = countryData.getName()!!
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.put(list.getJSONObject(i))
                }
            }
            results.values = nlist
            results.count = nlist.length()
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            dataArr = results.values as JSONArray
            notifyDataSetChanged()
        }
    }
}