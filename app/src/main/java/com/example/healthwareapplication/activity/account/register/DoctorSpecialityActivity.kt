package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_doctor_speciality
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import kotlinx.android.synthetic.main.activity_doctor_speciality.*

class DoctorSpecialityActivity : AppCompatActivity() {
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_doctor_speciality)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        userInfoTxt.text =
            userDetailModel.firstName.plus("\n").plus(userDetailModel.email).plus("\n")
                .plus(userDetailModel.mobile)
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
        if (specialityEdtTxt.text!!.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_speciality))
            isFlag = false
        }
        if (experienceEdtTxt.text!!.trim().isEmpty()) {
            AppHelper.showToast(this, getString(R.string.valid_experience))
            isFlag = false
        }
        if (isFlag) {
            goToNext()
        }
    }

    private fun goToNext() {
        val speciality = specialityEdtTxt.text.toString()
        val experience = experienceEdtTxt.text.toString()
        userDetailModel.speciality = speciality
        userDetailModel.experience = experience

        val intent = Intent(this, RegisterPasswordActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }

}