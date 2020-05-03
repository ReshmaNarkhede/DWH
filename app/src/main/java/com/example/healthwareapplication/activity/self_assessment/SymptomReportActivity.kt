package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions

class SymptomReportActivity : AppCompatActivity() {

    private lateinit var thankYouText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)

        initComponents()
        defaultConfiguration()
    }
    private fun initComponents() {
        AppHelper.transparentStatusBar(this)

        thankYouText = findViewById(R.id.thankYouText)
    }

    private fun defaultConfiguration() {
        val user = AppSessions.getLoginModel(this)
        thankYouText.text = "Report Generate"
    }

    fun okClick(view: View) {
        val intent = Intent(this, FeedbackActivity::class.java)
        startActivity(intent)
    }
}
