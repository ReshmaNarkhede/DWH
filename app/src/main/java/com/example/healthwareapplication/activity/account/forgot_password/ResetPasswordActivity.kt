package com.example.healthwareapplication.activity.account.forgot_password

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.model.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.LetsMeetActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityResetPasswordBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private var email: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        email = intent.getStringExtra(IntentConstants.kEMAIL)
    }

    private fun defaultConfiguration() {
        binding.okayBtn.setOnClickListener(View.OnClickListener {
            checkValidation()
        })
    }
    private fun checkValidation() {
        var isFlag = true
        val newPwd = binding.pwdEdtTxt.text.toString()
        val cnfmPwd = binding.cnfmPwdEdtTxt.text.toString()
        if (TextUtils.isEmpty(newPwd) || newPwd.length < 6) {
            AppHelper.showToast(this, getString(R.string.valid_password))
            isFlag = false
        } else if (TextUtils.isEmpty(cnfmPwd) || cnfmPwd.length < 6) {
            AppHelper.showToast(this, getString(R.string.valid_password))
            isFlag = false
        }
        if (isFlag) {
            if (newPwd == cnfmPwd) {
                fetchResetPwdApi(newPwd)
            } else {
                Toast.makeText(this, getString(R.string.password_not_match), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun fetchResetPwdApi(newPwd: String) {
            val apiService: ApiInterface =
                ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

            val param = JsonObject()
            param.addProperty("email_id", email)
            param.addProperty("password", newPwd)

            AppHelper.printParam("resetPWD param:", param)

            val call: Call<JsonObject> = apiService.resetPassword(param)
            call.enqueue(object : Callback<JsonObject?> {

                override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                    AppHelper.printUrl("resetPWD URL:", response)

                    if (response.isSuccessful) {
                        AppHelper.printResponse("resetPWD REs:", response)

                        val json = JSONObject(response.body().toString())
                        val responseModel = ResponseModel(json)
                        if (responseModel.isCode()) {
                            AppHelper.showToast(this@ResetPasswordActivity, responseModel.getMessage().toString())
                            val intent = Intent(this@ResetPasswordActivity, LetsMeetActivity::class.java)
                            startActivity(intent)
                        } else {
                            AppHelper.showToast(this@ResetPasswordActivity, responseModel.getMessage().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                    if (t is NoConnectivityException) {
                        AppHelper.showNetNotAvailable(this@ResetPasswordActivity)
                    }
                }
            })
        }

}