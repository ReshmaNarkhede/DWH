package com.example.healthwareapplication.activity.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_lets_meet
import com.example.healthwareapplication.activity.account.login.LoginActivity
import com.example.healthwareapplication.activity.account.register.RegisterAsActivity
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import kotlinx.android.synthetic.main.activity_lets_meet.*

class LetsMeetActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_lets_meet)

        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        val isAge = intent?.getBooleanExtra(IntentConstants.kLETS_MEET_MSG,false)
        if(isAge!!){
            letsMeetTxt.text = getString(R.string.age_no_16_msg)
        }else{
            letsMeetTxt.text = getString(R.string.lets_meet)
        }
    }

    fun signUpClick(view: View) {
        val intent = Intent(this, RegisterAsActivity::class.java)
        startActivity(intent)
    }
    fun LogInClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun forgotPwdClick(view: View) {}
}