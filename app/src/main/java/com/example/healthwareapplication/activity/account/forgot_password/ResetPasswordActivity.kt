package com.example.healthwareapplication.activity.account.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_reset_password
import com.example.healthwareapplication.activity.account.LetsMeetActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.cnfmPwdEdtTxt
import kotlinx.android.synthetic.main.activity_reset_password.pwdEdtTxt
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    private var email: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_reset_password)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        email = intent.getStringExtra(IntentConstants.kEMAIL)
    }

    private fun defaultConfiguration() {
        pwdLayout.setOnClickListener(View.OnClickListener {
            checkValidation()
        })
    }
    private fun checkValidation() {
        var isFlag = true
        val newPwd = pwdEdtTxt.text.toString()
        val cnfmPwd = cnfmPwdEdtTxt.text.toString()
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
                            val intent = Intent(this@ResetPasswordActivity,LetsMeetActivity::class.java)
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