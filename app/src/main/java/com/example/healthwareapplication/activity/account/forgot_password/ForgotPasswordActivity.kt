package com.example.healthwareapplication.activity.account.forgot_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.model.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.OtpActivity
import com.example.healthwareapplication.activity.account.login.LoginActivity
import com.example.healthwareapplication.activity.account.register.RegisterAsActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityForgotPasswordBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()

        binding.submitBtn.setOnClickListener { checkValidation() }
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    fun signUpClick(view: View) {
        val intent = Intent(this, RegisterAsActivity::class.java)
        startActivity(intent)
    }

    fun loginClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun forgotPwdClick(view: View) {
        finish()
    }

    fun parentCLick(view: View) {
        checkValidation()
    }

    private fun checkValidation() {
        var isFlag = false
        val email = binding.emailEdtTxt.text.trim().toString()
        if (email.isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_email))
            isFlag = false
        } else {
            if (email.matches(emailPattern.toRegex())) {
                isFlag = true
            }
        }
        if (isFlag) {
            fetchForgotPwdAPI(this, email)
        }
    }

    private fun fetchForgotPwdAPI(context: Context, email: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(context)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("email_id", email)

        val call: Call<JsonObject> = apiService.forgotPassword(param)

        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                if (response.isSuccessful) {
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
//                    if (responseModel.isCode()) {
                    val data = responseModel.getDataObj()
                    val otp = data?.optInt("otp")
                    showOTPDialog(context, email, otp.toString())
//                    }
//                    else{
//                        AppHelper.showToast(this@ForgotPasswordActivity,responseModel.getMessage().toString())
//                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(context)
                }
            }
        })
    }

    private fun showOTPDialog(context: Context, email: String, otp: String) {
//            val builder = AlertDialog.Builder(context)
//            builder.setMessage("Your Otp is: $otp")
//            builder.setPositiveButton("okay") { dialog, which ->
        val intent = Intent(context, OtpActivity::class.java)
        intent.putExtra(IntentConstants.kOTP, otp)
        intent.putExtra(IntentConstants.kEMAIL, email)
        intent.putExtra(IntentConstants.kIS_FORGOT, true)
        context.startActivity(intent)
//            }
//            val dialog: AlertDialog = builder.create()
//            dialog.show()
    }
}