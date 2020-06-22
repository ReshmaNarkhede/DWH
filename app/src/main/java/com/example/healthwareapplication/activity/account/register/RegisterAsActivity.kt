package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_register_as
import com.example.healthwareapplication.activity.account.login.LoginActivity
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import kotlinx.android.synthetic.main.activity_register_as.*

class RegisterAsActivity : AppCompatActivity(), View.OnClickListener {
    var userDetailModel: UserDetailModel = UserDetailModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_register_as)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {

        doctorBtn.setOnClickListener(this)
        humanBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.doctorBtn -> {
                userDetailModel.userType = 1
                openIntroActivity()
            }
            R.id.humanBtn -> {
                userDetailModel.userType = 2
                openIntroActivity()
            }
        }
    }

    private fun openIntroActivity() {
        val intent = Intent(this, GenderActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }

    fun signUpClick(view: View) {
        finish()
    }
}
