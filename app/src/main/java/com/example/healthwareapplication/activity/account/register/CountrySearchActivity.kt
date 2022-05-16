package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.country.SearchCountryAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityCountrySearchBinding
import com.example.healthwareapplication.model.response.ResponseModel
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountrySearchActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCountrySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountrySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {

        binding.searchTxt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    binding.cncleImg.visibility = View.VISIBLE
                    binding.cncleImg.setOnClickListener(this@CountrySearchActivity)
                } else {
                    binding.cncleImg.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.countryList.visibility = View.VISIBLE
                fetchCountryBySearch(s.toString())
            }
        })
    }

    private fun fetchCountryBySearch(search: String?) {
        val apiService: ApiInterface? =
            ApiClient.getRetrofitClient(this)?.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("country", search)

        val call: Call<JsonObject>? = apiService?.getCountry(param)
        call?.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                if (response.isSuccessful) {
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val countryListAry = responseModel.getDataArray()
                        if (countryListAry != null) {
                            bindCountrySearchName(countryListAry, search)
                        }
                    } else {
                        AppHelper.showToast(
                            this@CountrySearchActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@CountrySearchActivity)
                }
            }
        })
    }

    private fun bindCountrySearchName(countryAry: JSONArray, searchStr: String?) {
        binding.countryList.layoutManager = LinearLayoutManager(this)
        val adapter = SearchCountryAdapter(
            this, countryAry, searchStr
        ) { _, position ->
            val modelObj = JSONObject(countryAry.getJSONObject(position).toString())
            val resultIntent = Intent()
            resultIntent.putExtra(IntentConstants.kCOUNTRY_SELECTED, modelObj.toString())
            setResult(RESULT_OK, resultIntent)
            binding.searchTxt.text.clear()
            finish()
        }
        binding.countryList.adapter = adapter
    }

    fun backClick(view: View) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        DialogUtility.hideProgressDialog()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cncleImg -> {
                binding.searchTxt.text.clear()
                binding.countryList.visibility = View.GONE
            }
        }
    }
}