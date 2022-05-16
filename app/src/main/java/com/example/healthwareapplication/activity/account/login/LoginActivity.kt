package com.example.healthwareapplication.activity.account.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.model.response.ResponseModel
import com.example.healthwareapplication.activity.dashboard.DashboardActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.databinding.ActivityLoginBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        initListener()
    }

    private fun initListener() {
        binding.loginBtn.setOnClickListener {
            checkValidation()
        }
        binding.loginTxt.setOnClickListener {
            finish()
        }
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun checkValidation() {
        val email = binding.emailEdtTxt.text.trim().toString()
        val pwd = binding.pwdEdtTxt.text.trim().toString()
        when {
            email.isEmpty() -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter your email"
            }
            !email.matches(emailPattern.toRegex()) -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter your valid email"
            }
            pwd.isEmpty() -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter your password"
            }
            else -> {
                binding.errorText.visibility = View.INVISIBLE
                fetchLoginAPI(email, pwd)
            }
        }
    }

    private fun fetchLoginAPI(uName: String, pwd: String) {
        val apiService: ApiInterface? =
            ApiClient.getRetrofitClient(this)?.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("username", uName)
        param.addProperty("password", pwd)

        val call: Call<JsonObject>? = apiService?.fetchLogin(param)
        call?.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                Log.e("LOGIN: $param", ": " + response.raw().request.url)
                if (response.isSuccessful) {
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val detailObj = responseModel.getDataObj()
                        AppSettings.setBooleanValue(
                            this@LoginActivity,
                            AppConstants.kIS_LOGIN,
                            true
                        )
                        AppSettings.setJsonObjectValue(
                            this@LoginActivity,
                            AppConstants.kLOGIN,
                            detailObj.toString()
                        )

                        showDashboard()
                        binding.errorText.visibility = View.GONE
                    } else {
                        binding.errorText.visibility = View.VISIBLE
//                        binding.errorText.text = responseModel.getMessage()
                        binding.errorText.text = getString(R.string.unauthorized)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
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
