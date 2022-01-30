package com.example.healthwareapplication.activity.account.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.dashboard.DashboardActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OldLoginActivity : AppCompatActivity() {
    private lateinit var uniqueIdLayout: LinearLayout
    private lateinit var passwordLayout: LinearLayout
    private lateinit var uniqueId: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var countryCode: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_login)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        uniqueIdLayout = findViewById(R.id.uniqueIdLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        uniqueId = findViewById(R.id.uniqueId)
        password = findViewById(R.id.password)
        countryCode = findViewById(R.id.countryCode)
    }

    private fun defaultConfiguration() {
//        val countryModel = AppSessions.getCountry(this)
//        countryCode.text = countryModel!!.getCode()
        uniqueId.hint = getString(R.string.enter_your_mobile_no)
    }

    fun acha_click(v: View) {
        if (uniqueId.text.toString() != "") {
            uniqueIdLayout.visibility = View.GONE
            passwordLayout.visibility = View.VISIBLE
        } else {
            uniqueId.error = resources.getString(R.string.errormessage)
        }

    }

    fun letsStartClick(v: View) {
        if (password.text.toString().trim() != "") {
            fetchLogin(uniqueId.text!!.trim().toString(), password.text!!.trim().toString())
        } else {
            password.error = resources.getString(R.string.errormessage)
        }
    }

    private fun fetchLogin(uName: String, pwd: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("userName", uName)
        param.addProperty("password", pwd)

        val call: Call<JsonObject> = apiService.fetchLogin(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                Log.e("LOGIN: $param",": "+ response.raw().request.url)
                if (response.isSuccessful) {
                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val detailObj = responseModel.getDataObj()
                        val gender = detailObj!!.optString("sex").toString()
                        AppSettings.setBooleanValue(this@OldLoginActivity, AppConstants.kIS_LOGIN, true)
                        AppSettings.setStringValue(this@OldLoginActivity, AppConstants.kUSER_GENDER, gender)
                        AppSettings.setJsonObjectValue(this@OldLoginActivity, AppConstants.kLOGIN, detailObj.toString())

//                        fetchApidata()

                        showDashboard()
                    }
                    else{
                        AppHelper.showToast(this@OldLoginActivity,responseModel.getMessage().toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@OldLoginActivity)
                }
            }
        })
    }

    private fun fetchApidata() {

    }

    private fun showDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
