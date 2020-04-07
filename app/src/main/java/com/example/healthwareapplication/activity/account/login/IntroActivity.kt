package com.example.healthwareapplication.activity.account.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel

class IntroActivity : AppCompatActivity() {

    private lateinit var welcomeMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        welcomeMessage = findViewById(R.id.welcomeMessage)
    }

    private fun defaultConfiguration() {
        val userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA)!! as UserDetailModel
        if (userDetailModel.userType == 3) {
            welcomeMessage.text = resources.getString(R.string.welcome_message_dr)
        } else {
            welcomeMessage.text = resources.getString(R.string.welcome_message)
        }
    }

    fun okehClick(v: View) {
        val intent = Intent(this, HaveWeMetActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, this.intent?.getSerializableExtra(IntentConstants.kUSER_DATA))
        startActivity(intent)
    }
}
