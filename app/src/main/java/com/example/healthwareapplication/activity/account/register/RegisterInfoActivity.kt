package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityRegisterInfoBinding
import com.example.healthwareapplication.model.user.UserDetailModel

class RegisterInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterInfoBinding
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        binding.cityTxt.text = userDetailModel.cityName.plus(", ").plus(userDetailModel.countryName)
        binding.cityTxt.setOnClickListener(View.OnClickListener {
            finish()
        })
        val maxLengthofEditText = userDetailModel.mobileLength!!.toInt()
        binding.mobNoEdtTxt.filters = arrayOf<InputFilter>(LengthFilter(maxLengthofEditText))
        binding.prefix.text = userDetailModel.mobilePrefix
    }

    private fun defaultConfiguration() {
        binding.parentLayout.setOnClickListener(View.OnClickListener {
            checkValidation()
        })
        binding.mobNoEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) {
                    binding.prefix.visibility = View.VISIBLE
                } else {
                    binding.prefix.visibility = View.GONE
                }
            }
        })
        binding.nameEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) {
                    if (userDetailModel.userType == 1) {
                        binding.drPrefix.visibility = View.VISIBLE
                    } else {
                        binding.drPrefix.visibility = View.GONE
                    }
                } else {
                    binding.drPrefix.visibility = View.GONE
                }
            }
        })
    }

    private fun checkValidation() {
        var isFlag = true
        if (binding.nameEdtTxt.text.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_name))
            isFlag = false
        }
        if (binding.emailEdtTxt.text.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_email))
            isFlag = false
        } else {
            if (binding.emailEdtTxt.text.trim().matches(emailPattern.toRegex())) {
                isFlag = true
            }
        }
        if (binding.mobNoEdtTxt.text.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.please_enter_mob_no))
            isFlag = false
        } else {
            if (AppHelper.isValidMobile(binding.mobNoEdtTxt.text!!.toString())) {
                isFlag = true
            }
        }
        if (isFlag) {
            goToNext()
        }
    }

    private fun goToNext() {
        val name = binding.nameEdtTxt.text.toString()
        val email = binding.emailEdtTxt.text.toString()
        val mobNo = binding.mobNoEdtTxt.text.toString()
        userDetailModel.firstName = name
        userDetailModel.email = email
        userDetailModel.mobile = mobNo

        if (userDetailModel.userType == 1) {
            val intent = Intent(this, DoctorSpecialityActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        } else if (userDetailModel.userType == 2) {
            val intent = Intent(this, RegisterPasswordActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        }
    }

}
