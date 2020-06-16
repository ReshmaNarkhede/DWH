package com.example.healthwareapplication.activity.account.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R.layout.activity_login

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
    }

    private fun defaultConfiguration() {

    }

    fun loginClick(view: View) {
        finish()
    }
}
