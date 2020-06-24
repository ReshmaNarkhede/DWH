package com.example.healthwareapplication.activity.account.forgot_password

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_forgot_password
import com.example.healthwareapplication.activity.account.OtpActivity
import com.example.healthwareapplication.activity.account.login.LoginActivity
import com.example.healthwareapplication.activity.account.register.RegisterAsActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiData
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_forgot_password)

        initComponents()
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
        val email = emailEdtTxt.text.trim().toString()
        if (email.isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_email))
            isFlag = false
        } else {
            if (email.matches(emailPattern.toRegex())) {
                isFlag = true
            }
        }
        if (isFlag) {
            ApiData.fetchForgotPwdAPI(this,email)
        }
    }

}