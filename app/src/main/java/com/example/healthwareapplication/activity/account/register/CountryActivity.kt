package com.example.healthwareapplication.activity.account.register


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.country.CityAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityCountryBinding
import com.example.healthwareapplication.model.country.CityData
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountryBinding
    private lateinit var userDetailModel: UserDetailModel
    private var dataArray: JSONArray? = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        dataArray = AppSessions.getCountryArray(this)!!
        userDetailModel =
            intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
    }

    private fun defaultConfiguration() {
        if (userDetailModel.tob.isEmpty() || userDetailModel.tob == getString(R.string.time)) {
            binding.dobTxt.text = AppHelper.getDobFormat(userDetailModel.dob)
        } else {
            binding.dobTxt.text = AppHelper.getDobFormat(userDetailModel.dob).plus(" ").plus(userDetailModel.tob)
        }

        binding.cityTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if(s.toString().isNotEmpty()) {
                    binding.cityList.visibility = View.VISIBLE
                    binding.cityLayout.background = resources.getDrawable(R.drawable.country_btn_selector)
                    callCityApi(s.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if(editable.toString().isEmpty()){
                    binding.cityList.visibility = View.GONE
                    binding.cityLayout.background = resources.getDrawable(R.drawable.btn_selector)
                }
            }
        })
    }

    private fun callCityApi(searchStr: String?) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("city", searchStr)

        val call: Call<JsonObject> = apiService.getCity(param)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                Log.d("COUNTRY: ", ": " + response.raw().request.url)
                if (response.isSuccessful) {
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        dataArray = responseModel.getDataArray()
                        AppSettings.setJsonArrayValue(
                            this@CountryActivity,
                            AppConstants.kCOUNTRY_DATA,
                            dataArray.toString()
                        )
                        bindCountryData(dataArray, searchStr)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@CountryActivity)
                }
            }
        })
    }

    private fun bindCountryData(dataArray: JSONArray?, searchStr: String?) {
        val linearlayoutManager = LinearLayoutManager(this, VERTICAL, true)
        linearlayoutManager.reverseLayout = true
        binding.cityList.layoutManager = linearlayoutManager
        val adapter = CityAdapter(this, dataArray!!, searchStr,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                binding.cityList.visibility = View.GONE
                val model = CityData(dataArray.getJSONObject(position))
                binding.countryTxt.setText(model.getCountry())
                binding.cityTxt.setText(model.getName())
                userDetailModel.cityId = model.getId()!!.toInt()
                userDetailModel.cityName = model.getName()
                userDetailModel.countryName = model.getCountry()
                userDetailModel.mobileLength = model.getDigits()
                userDetailModel.mobilePrefix = "+"+model.getCode()
                val intent = Intent(this, RegisterInfoActivity::class.java)
                intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
                startActivity(intent)
                binding.cityTxt.text.clear()
                binding.countryTxt.text.clear()
            })
        binding.cityList.setHasFixedSize(true)
        binding.cityList.adapter = adapter
    }

    fun dobClick(view: View) {
        finish()
    }

    override fun onResume() {
        super.onResume()
        binding.cityList.visibility = View.GONE
        binding.cityLayout.background = resources.getDrawable(R.drawable.btn_selector)
    }
}
