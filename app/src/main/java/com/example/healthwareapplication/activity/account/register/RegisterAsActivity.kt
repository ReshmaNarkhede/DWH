package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityRegisterAsBinding
import com.example.healthwareapplication.model.user.UserDetailModel

class RegisterAsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterAsBinding
    var userDetailModel: UserDetailModel = UserDetailModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {
        binding.doctorBtn.setOnClickListener(this)
        binding.humanBtn.setOnClickListener(this)
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
