package com.example.healthwareapplication.activity.account.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel

class LoginAsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var doctorBtn: Button
    private lateinit var userBtn: Button
    var userDetailModel: UserDetailModel = UserDetailModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_as)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        doctorBtn = findViewById(R.id.doctorBtn)
        userBtn = findViewById(R.id.userBtn)
    }

    private fun defaultConfiguration() {

        doctorBtn.setOnClickListener(this)
        userBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.userBtn -> {
                userDetailModel.userType = 1
                openIntroActivity()
            }
            R.id.doctorBtn -> {
                userDetailModel.userType = 3
                openIntroActivity()
            }
        }
    }

    private fun openIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }
}
