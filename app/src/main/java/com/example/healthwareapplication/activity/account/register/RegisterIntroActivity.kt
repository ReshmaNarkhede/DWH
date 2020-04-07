package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel

class RegisterIntroActivity : AppCompatActivity() {
    private lateinit var userDetailModel: UserDetailModel
    private lateinit var info: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_intro)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        info = findViewById(R.id.info)
    }

    private fun defaultConfiguration() {
        userDetailModel = intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        if (userDetailModel.userType == 3) {
            info.text = resources.getString(R.string.register_intro_msg_dr)
        } else {
            info.text = resources.getString(R.string.register_intro_msg)
        }
    }

    fun hmmClick(view: View) {
        val intent = Intent(this, GenderActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }
}
