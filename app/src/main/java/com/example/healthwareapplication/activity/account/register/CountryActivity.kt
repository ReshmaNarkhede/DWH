package com.example.healthwareapplication.activity.account.register


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_country
import com.example.healthwareapplication.adapter.country.CityAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.country.CityData
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_country.*
import kotlinx.android.synthetic.main.activity_country.cityTxt
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var userDetailModel: UserDetailModel
    private var dataArray: JSONArray? = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_country)

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
            dobTxt.text = userDetailModel.dob
        } else {
            dobTxt.text = userDetailModel.dob.plus(" ").plus(userDetailModel.tob)
        }

        cityTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                cityList.visibility = View.VISIBLE
                callCityApi(s.toString())
            }

            override fun afterTextChanged(editable: Editable) {
                Log.e("City: ", ": " + cityTxt.text.toString())
            }
        })
//        cityPLayout.setOnClickListener(this)
    }

    private fun callCityApi(searchStr: String?) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("city", searchStr)

        val call: Call<JsonObject> = apiService.getCity(param)
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
        cityList.layoutManager = LinearLayoutManager(this)
        val adapter = CityAdapter(this, dataArray!!, searchStr,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                cityList.visibility = View.GONE
                val model = CityData(dataArray.getJSONObject(position))
                countryTxt.setText(model.getCountry())
                cityTxt.setText(model.getName())
                userDetailModel.cityId = model.getId()
                userDetailModel.cityName = model.getName()
                userDetailModel.countryName = model.getCountry()
                userDetailModel.mobileLength = model.getDigits()
                val intent = Intent(this, RegisterInfoActivity::class.java)
                intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
                startActivity(intent)
            })
        cityList.adapter = adapter
    }

    fun dobClick(view: View) {
        finish()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.cityPLayout -> {
//                checkValidation()
            }
        }
    }

//    private fun checkValidation() {
//        var isFlag = true
//        if (cityTxt.text!!.trim().isEmpty()) {
//            AppHelper.showToast(this, getString(R.string.valid_city))
//            isFlag = false
//        }
//        if (countryTxt.text!!.trim().isEmpty()) {
//            AppHelper.showToast(this, getString(R.string.valid_country))
//            isFlag = false
//        }
//        if (isFlag) {
//            goToNext()
//        }
//    }
//
//    private fun goToNext() {
//        userDetailModel.cityId = cityTxt.text.toString()
//        val intent = Intent(this, RegisterInfoActivity::class.java)
//        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
//        startActivity(intent)
//    }
}
