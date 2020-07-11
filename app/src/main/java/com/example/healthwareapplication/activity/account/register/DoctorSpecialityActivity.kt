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
import com.example.healthwareapplication.R.layout.activity_doctor_speciality
import com.example.healthwareapplication.adapter.country.SpecialityAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.country.CityData
import com.example.healthwareapplication.model.country.SpecialityData
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_country.*
import kotlinx.android.synthetic.main.activity_doctor_speciality.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DoctorSpecialityActivity : AppCompatActivity() {
    private var specArray: JSONArray? = JSONArray()
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_doctor_speciality)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        userInfoTxt.text =
            userDetailModel.firstName.plus("\n").plus(userDetailModel.email).plus("\n")
                .plus(userDetailModel.mobile)
        userInfoTxt.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun defaultConfiguration() {
        pwdParentLayout.setOnClickListener(View.OnClickListener {
            checkValidation()
        })
        specialityEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if(s.toString().isNotEmpty()) {
                    specialityList.visibility = View.VISIBLE
                    specLayout.background = resources.getDrawable(R.drawable.country_btn_selector)
                    fetchSpeciality(s.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if(editable.toString().isEmpty()){
                    specialityList.visibility = View.GONE
                    specLayout.background = resources.getDrawable(R.drawable.btn_selector)
                }
            }
        })
    }

    private fun checkValidation() {
        var isFlag = true
        if (specialityEdtTxt.text!!.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_speciality))
            isFlag = false
        }
        if (experienceEdtTxt.text!!.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_experience))
            isFlag = false
        }
        if (isFlag) {
            goToNext()
        }
    }

    private fun goToNext() {
        val speciality = specialityEdtTxt.text.toString()
        val experience = experienceEdtTxt.text.toString()
        userDetailModel.speciality = speciality
        userDetailModel.experience = experience

        val intent = Intent(this, RegisterPasswordActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)

        specialityEdtTxt.text.clear()
    }

    private fun bindData(specAry: JSONArray?,searchStr: String) {
        val linearlayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        linearlayoutManager.reverseLayout = true
        specialityList.layoutManager = linearlayoutManager
        val adapter = SpecialityAdapter(this, specAry!!,searchStr,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val model = SpecialityData(specAry.getJSONObject(position))
                specialityEdtTxt.setText(model.getName())
                specialityList.visibility = View.GONE
                specLayout.background = resources.getDrawable(R.drawable.btn_selector)
            })
        specialityList.setHasFixedSize(true)
        specialityList.adapter = adapter

    }

    private fun fetchSpeciality(searchStr: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("search", searchStr)

        val call: Call<JsonObject> = apiService.getSpeciality(param)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("Speciality url ", response)
                if (response.isSuccessful) {
                    AppHelper.printResponse("Speciality Res ", response)

                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        specArray = responseModel.getDataArray()
//                        val gson = GsonBuilder().create()
//                        val list =
//                            gson.fromJson(tempAry.toString(), Array<SpecialityModel>::class.java)
//                                .toList()
//                        Log.e("LIst:", "${list.size}")
                        bindData(specArray,searchStr)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@DoctorSpecialityActivity)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
//        specialityEdtTxt.text.clear()
        specialityList.visibility = View.GONE
        specLayout.background = resources.getDrawable(R.drawable.btn_selector)
    }
}