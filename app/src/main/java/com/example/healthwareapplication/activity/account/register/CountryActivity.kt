package com.example.healthwareapplication.activity.account.register


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R

import com.example.healthwareapplication.adapter.country.CountryAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants

import com.example.healthwareapplication.model.country.CountryData

import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_country.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryActivity : AppCompatActivity() {

    private var dataArray: JSONArray? = JSONArray()
    private lateinit var adapter: CountryAdapter

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
        dataArray = AppSessions.getCountryArray(this)!!
        if(dataArray!!.length()>0){
            bindCountryData(dataArray)
        }else{
            callCountryApi()
        }
        cityTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                cityList.visibility = View.VISIBLE
                adapter.getFilter().filter(charSequence.trim())
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
    }


    private fun callCountryApi() {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)
        val call: Call<JsonObject> = apiService.getCountry()
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                Log.d("COUNTRY: ",": "+response.raw().request().url())
                if (response.isSuccessful) {
                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        dataArray = responseModel.getDataArray()
                        AppSettings.setJsonArrayValue(this@CountryActivity,AppConstants.kCOUNTRY_DATA,dataArray.toString())
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
        cityList.layoutManager = LinearLayoutManager(this)
        adapter = CountryAdapter(
                dataArray!!,
                RecyclerItemClickListener.OnItemClickListener { view, position ->
                    cityList.visibility = View.GONE
                    val model = CountryData(dataArray.getJSONObject(position))
                    finish()
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
//                        AppSettings.setJsonObjectValue(
//                            this,
//                            AppConstants.kCOUNTRY,
//                            dataArray.getJSONObject(position).toString()
//                        )
//                        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
//                        startActivity(intent)
//                    }
                })
        cityList.adapter = adapter
    }
}
