package com.example.healthwareapplication.activity.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R.layout.activity_lets_meet
import com.example.healthwareapplication.activity.account.login.LoginActivity

class LetsMeetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_lets_meet)
    }

    fun signUpClick(view: View) {}
    fun LogInClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun forgotPwdClick(view: View) {}
}