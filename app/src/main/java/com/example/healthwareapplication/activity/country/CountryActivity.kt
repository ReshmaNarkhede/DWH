package com.example.healthwareapplication.activity.country

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.country.CountryAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.country.CountryData
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_country.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CountryActivity : AppCompatActivity(), RecyclerItemClickListener.OnItemClickListener {

    private var dataArray: JSONArray? = JSONArray()
    private lateinit var adapter: CountryAdapter
    private lateinit var countryList: RecyclerView
    private lateinit var countrySearch: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {
        val dobDate = intent.getStringExtra(IntentConstants.kDOB_DATE)
        val dobTime = intent.getStringExtra(IntentConstants.kDOB_TIME)
        dobTxt.text = dobDate!!.plus(" ").plus(dobTime)
//        dataArray = AppSessions.getCountryArray(this)!!
//        if(dataArray!!.length()>0){
//            bindCountryData(dataArray)
//        }else{
        callCountryApi()
//        }
//        countrySearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//                adapter.getFilter().filter(charSequence.trim())
//            }
//
//            override fun afterTextChanged(editable: Editable) {
//            }
//        })
    }


    private fun callCountryApi() {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)
        val call: Call<JsonObject> = apiService.getCountry()
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                Log.d("COUNTRY: ", ": " + response.raw().request().url())
                if (response.isSuccessful) {
                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        dataArray = responseModel.getDataArray()
                        AppSettings.setJsonArrayValue(
                            this@CountryActivity,
                            AppConstants.kCOUNTRY_DATA,
                            dataArray.toString()
                        )
                        bindCountryData(dataArray)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@CountryActivity)
                }
            }
        })
    }

    private fun bindCountryData(dataArray: JSONArray?) {
        val countryAdapter = CAdapter(dataArray)
        countryTxt.setAdapter(countryAdapter)
        countryTxt.threshold = 1
//        countryList.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
//        adapter =
//            CountryAdapter(
//                dataArray!!,
//                RecyclerItemClickListener.OnItemClickListener { view, position ->
//
//                    val model = CountryData(dataArray.getJSONObject(position))
//
//                    if (intent.getIntExtra(IntentConstants.kUSER_TYPE, 0) == 1) {
//                        AppSettings.setJsonObjectValue(
//                            this,
//                            AppConstants.kCOUNTRY,
//                            dataArray.getJSONObject(position).toString()
//                        )
//                        val intent = Intent(this, OldLoginActivity::class.java)
//                        startActivity(intent)
//                    } else {
//                        val userDetailModel =
//                            intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
//                        userDetailModel.countryId = model.getId()
//                        val intent = Intent(this, RegisterMobNoActivity::class.java)
//                        AppSettings.setJsonObjectValue(this, AppConstants.kCOUNTRY, dataArray.getJSONObject(position).toString())
//                        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
//                        startActivity(intent)
//                    }
//                })
//        countryList.adapter = adapter
    }

    fun dobClick(view: View) {
        finish()
    }

    override fun onItemClick(view: View?, position: Int) {

    }

    inner class CAdapter(dataArray: JSONArray?) : BaseAdapter(), Filterable {
        private val filter: Filter = CustomFilter()
        var dataArr :JSONArray? = dataArray
        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val inflater = LayoutInflater.from(this@CountryActivity)
            var convertView:View? = view
            val holder: ViewHolder

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.symptom_list_item, parent, false)
                holder = ViewHolder()
                holder.autoText = convertView!!.findViewById(R.id.symptomName) as TextView
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            holder.autoText!!.text = CountryData(dataArr!!.getJSONObject(position)).getName()!!

            return convertView
        }

        override fun getItem(position: Int): Any {
            return CountryData(dataArr!!.getJSONObject(position)).getName()!!
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return if (dataArr == null) 0 else dataArr!!.length()
        }

        override fun getFilter(): Filter {
            return filter
        }

        inner class CustomFilter : Filter() {
            override fun performFiltering(constraint: CharSequence): Filter.FilterResults {

                val filterString = constraint.toString().toLowerCase()

                val results = Filter.FilterResults()

                val list = dataArr

                val count = list!!.length()
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
                dataArr = results.values!! as JSONArray?
                Log.e("Size: ",": $dataArr")
                notifyDataSetChanged()
            }
        }
    }

    private class ViewHolder {
        var autoText: TextView? = null
    }
}
