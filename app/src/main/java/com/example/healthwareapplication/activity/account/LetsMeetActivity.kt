package com.example.healthwareapplication.activity.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_lets_meet
import com.example.healthwareapplication.activity.account.forgot_password.ForgotPasswordActivity
import com.example.healthwareapplication.activity.account.login.LoginActivity
import com.example.healthwareapplication.activity.account.register.RegisterAsActivity
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityLetsMeetBinding

class LetsMeetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetsMeetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetsMeetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        val isAge = intent?.getBooleanExtra(IntentConstants.kLETS_MEET_MSG, false)
        if (isAge == true) {
            binding.apply {
                answerTxt.visibility = View.VISIBLE
                answerTxt.text = getString(R.string.no)
                answerTxt.setOnClickListener(View.OnClickListener {
                    finish()
                })
                letsMeetTxt.text = getString(R.string.age_no_16_msg)
            }
        } else {
            binding.apply {
                answerTxt.visibility = View.GONE
                letsMeetTxt.text = getString(R.string.lets_meet)
            }
        }
    }

    fun signUpClick(view: View) {
        val intent = Intent(this, RegisterAsActivity::class.java)
        startActivity(intent)
    }

    fun logInClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun forgotPwdClick(view: View) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}