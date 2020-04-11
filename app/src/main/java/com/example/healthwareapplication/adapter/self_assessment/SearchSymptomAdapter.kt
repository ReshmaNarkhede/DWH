package com.example.healthwareapplication.adapter.self_assessment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.healthwareapplication.model.self_assessment.SymptomDataModel
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel

class SearchSymptomAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val jsonArray: List<SymptomDataModel>) :

    ArrayAdapter<SymptomDataModel>(context, layoutResource, jsonArray){
//    , Filterable {
    private var newList: List<SymptomDataModel> = jsonArray
    var mFilter: SearchSymptomAdapter.ItemFilter = ItemFilter()

    override fun getCount(): Int {
        return newList.size
    }

    override fun getItem(p0: Int): SymptomDataModel? {
        return newList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return newList.get(p0).id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context)
            .inflate(layoutResource, parent, false) as TextView
        view.text = newList[position].name
        return view

    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {

            val filterString = constraint.toString().toLowerCase()

            val results = Filter.FilterResults()

            val list = newList

            val count = list.size
            val nlist = ArrayList<SymptomDataModel>(count)

            var filterableString: String

            for (i in 0 until count) {
                val model = list[i]
                filterableString = model.name!!
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list[i])
                }
            }
            results.values = nlist
            results.count = nlist.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            newList = results.values as List<SymptomDataModel>
            notifyDataSetChanged()
        }
    }
}
