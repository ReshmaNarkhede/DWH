package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.country.CityAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityCitySearchBinding
import com.example.healthwareapplication.model.response.ResponseModel
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitySearchActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCitySearchBinding
    private var country: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        country = intent.getStringExtra(IntentConstants.kCOUNTRY_SELECTED)
        Log.d("country","$country")
    }

    private fun defaultConfiguration() {

        binding.searchTxt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    binding.cncleImg.visibility = View.VISIBLE
                    binding.cncleImg.setOnClickListener(this@CitySearchActivity)
                } else {
                    binding.cncleImg.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.cityList.visibility = View.VISIBLE
                fetchCityByCountry(s.toString())
            }
        })
    }

    private fun fetchCityByCountry(search: String?) {
        val apiService: ApiInterface? =
            ApiClient.getRetrofitClient(this)?.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("country", country)
        param.addProperty("city", search)
        AppHelper.printParam("SEARCH ", param)

        val call: Call<JsonObject>? = apiService?.getCityByCountry(param)
        call?.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("SEARCH URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("SEARCH REs:", response)

                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val cityListAry = responseModel.getDataArray()
                        if (cityListAry != null) {
                            bindCityByCountry(cityListAry, search)
                        }
                    } else {
                        AppHelper.showToast(
                            this@CitySearchActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@CitySearchActivity)
                }
            }
        })
    }

    private fun bindCityByCountry(cityAry: JSONArray, searchStr: String?) {
        binding.cityList.layoutManager = LinearLayoutManager(this)
        val adapter = CityAdapter(
            this, cityAry, searchStr
        ) { _, position ->
            val modelObj = JSONObject(cityAry.getJSONObject(position).toString())
            val resultIntent = Intent()
            resultIntent.putExtra(IntentConstants.kCITY_SELECTED, modelObj.toString())
            setResult(RESULT_OK, resultIntent)
            binding.searchTxt.text.clear()
            finish()
        }
        binding.cityList.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        DialogUtility.hideProgressDialog()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cncleImg -> {
                binding.searchTxt.text.clear()
                binding.cityList.visibility = View.GONE
            }
        }
    }
}