package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.android.material.textfield.TextInputEditText

class RegisterPasswordActivity : AppCompatActivity() {
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_password)
        initComponents()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
    }

    fun doneClick(view: View) {
        if (password.text.toString().equals("")) {
            password.error = resources.getString(R.string.errormessage)
        } else if (confirmPassword.text.toString().equals("") && confirmPassword.text.toString()
                .equals(password.text.toString())
        ) {
            confirmPassword.error = resources.getString(R.string.errormessage)
        } else {
            if (intent.getIntExtra(IntentConstants.kUSER_TYPE, 0) == 1) {
                //TODO change password API
//                networkCalls.changePassword(this, SendOtpModel(registrationPasswordActivity.intent.getStringExtra("id"), "", registrationPasswordActivity.password.text.trim().toString()))
            } else {
                val userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
                userDetailModel.password = password.text!!.trim().toString()
                val intent = Intent(this, RegisterTermsActivity::class.java)
                intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
                startActivity(intent)
            }
        }
    }
}
