package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import kotlinx.android.synthetic.main.activity_register_info.*


class RegisterInfoActivity : AppCompatActivity() {

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        cityTxt.text = userDetailModel.cityName.plus(", ").plus(userDetailModel.countryName)
        cityTxt.setOnClickListener(View.OnClickListener {
            finish()
        })
        val maxLengthofEditText = userDetailModel.mobileLength!!.toInt()
        mobNoEdtTxt.filters = arrayOf<InputFilter>(LengthFilter(maxLengthofEditText))
    }

    private fun defaultConfiguration() {
        parentLayout.setOnClickListener(View.OnClickListener {
            checkValidation()
        })
    }

    private fun checkValidation() {
        var isFlag = false
        if (nameEdtTxt.text!!.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_name))
            isFlag = false
        }
        if (emailEdtTxt.text!!.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_email))
            isFlag = false
        } else {
            if (emailEdtTxt.text!!.trim().matches(emailPattern.toRegex())) {
                isFlag = true
            }else{
                AppHelper.showToast(this, getString(R.string.valid_email))
                isFlag = false
            }
        }
        if (mobNoEdtTxt.text!!.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.please_enter_mob_no))
            isFlag = false
        }
        if (isFlag) {
            goToNext()
        }
    }

    private fun goToNext() {
        val name = nameEdtTxt.text.toString()
        val email = emailEdtTxt.text.toString()
        val mobNo = mobNoEdtTxt.text.toString()
        userDetailModel.firstName = name
        userDetailModel.email = email
        userDetailModel.mobile = mobNo

        if (userDetailModel.userType == 1) {
            val intent = Intent(this, DoctorSpecialityActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        } else if(userDetailModel.userType == 2){
            val intent = Intent(this, RegisterPasswordActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        }
    }

}
