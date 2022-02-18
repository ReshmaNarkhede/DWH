package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
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
        binding.ageTxt.setOnClickListener {
            finish()
        }
    }

    private fun defaultConfiguration() {
        binding.okayBtn.setOnClickListener {
            checkValidation()
        }

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
        if (isFlag) {
            goToNext()
        }
    }

    private fun goToNext() {
        val name = binding.nameEdtTxt.text.toString()
//        val email = binding.emailEdtTxt.text.toString()
//        val mobNo = binding.mobNoEdtTxt.text.toString()
        userDetailModel.firstName = name
//        userDetailModel.email = email
//        userDetailModel.mobile = mobNo

        if (userDetailModel.userType == 1) {
            val intent = Intent(this, DoctorSpecialityActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        } else if (userDetailModel.userType == 2) {
            val intent = Intent(this, GenderActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        }
    }

}
