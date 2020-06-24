package com.example.healthwareapplication.activity.account.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_login
import com.example.healthwareapplication.activity.dashboard.DashboardActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.AppConstants
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var pwd: String
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)

        initComponents()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    fun loginClick(view: View) {
        finish()
    }

    fun parentCLick(view: View) {
        checkValidation()
    }

    private fun checkValidation() {
        var isFlag = false
        val email = emailEdtTxt.text.trim().toString()
        val pwd = pwdEdtTxt.text.trim().toString()
        if (email.isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_email))
            isFlag = false
        } else {
            if (email.matches(emailPattern.toRegex())) {
                isFlag = true
            }
        }
        if (pwd.isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_password))
            isFlag = false
        }
        if (isFlag) {
            fetchLoginAPI(email,pwd)
        }
    }

    private fun fetchLoginAPI(uName: String, pwd: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("username", uName)
        param.addProperty("password", pwd)

        val call: Call<JsonObject> = apiService.fetchLogin(param)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                Log.e("LOGIN: $param",": "+response.raw().request().url())
                if (response.isSuccessful) {
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val detailObj = responseModel.getDataObj()
                        val gender = detailObj!!.optString("sex").toString()
                        AppSettings.setBooleanValue(this@LoginActivity, AppConstants.kIS_LOGIN, true)
                        AppSettings.setStringValue(this@LoginActivity, AppConstants.kUSER_GENDER, gender)
                        AppSettings.setJsonObjectValue(this@LoginActivity, AppConstants.kLOGIN, detailObj.toString())

                        showDashboard()
                    }
                    else{
                        AppHelper.showToast(this@LoginActivity,responseModel.getMessage().toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@LoginActivity)
                }
            }
        })
    }
    private fun showDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

}
