package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_register_password
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import kotlinx.android.synthetic.main.activity_register_info.*
import kotlinx.android.synthetic.main.activity_register_password.*

class RegisterPasswordActivity : AppCompatActivity() {
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_register_password)
        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        userInfoTxt.text = userDetailModel.firstName.plus("\n").plus(userDetailModel.email).plus("\n").plus(userDetailModel.mobile)
        userInfoTxt.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun defaultConfiguration() {
        pwdParentLayout.setOnClickListener(View.OnClickListener {
            checkValidation()
        })
    }

    private fun checkValidation() {
        var isFlag = true
        val newPwd = pwdEdtTxt.text.toString()
        val cnfmPwd = cnfmPwdEdtTxt.text.toString()
        if (TextUtils.isEmpty(newPwd) || newPwd.length<6) {
            AppHelper.showToast(this,getString(R.string.valid_password))
            isFlag = false
        } else if (TextUtils.isEmpty(cnfmPwd) || cnfmPwd.length<6) {
            AppHelper.showToast(this,getString(R.string.valid_password))
            isFlag = false
        }
        if (isFlag) {
            if (newPwd == cnfmPwd) {
                goToNext()
            } else {
                Toast.makeText(this, getString(R.string.password_not_match), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun goToNext() {
        val pwd = pwdEdtTxt.text.toString()
        userDetailModel.password = pwd

        val intent = Intent(this, RegisterTermsActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }


//    fun doneClick(view: View) {
//        if (password.text.toString().equals("")) {
//            password.error = resources.getString(R.string.errormessage)
//        } else if (confirmPassword.text.toString().equals("") && confirmPassword.text.toString()
//                .equals(password.text.toString())
//        ) {
//            confirmPassword.error = resources.getString(R.string.errormessage)
//        } else {
//            if (intent.getIntExtra(IntentConstants.kUSER_TYPE, 0) == 1) {
//                //TODO change password API
////                networkCalls.changePassword(this, SendOtpModel(registrationPasswordActivity.intent.getStringExtra("id"), "", registrationPasswordActivity.password.text.trim().toString()))
//            } else {
//                val userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
//                userDetailModel.password = password.text!!.trim().toString()
//                val intent = Intent(this, RegisterTermsActivity::class.java)
//                intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
//                startActivity(intent)
//            }
//        }
//    }
}
