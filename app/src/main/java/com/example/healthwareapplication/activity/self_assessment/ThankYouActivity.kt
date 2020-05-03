package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.constants.IntentConstants
import com.google.gson.Gson

class ThankYouActivity : AppCompatActivity() {

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
        thankYouText.text = "Thank you ${user!!.firstName} All done! Your Report is Ready"
    }

    fun okClick(view: View) {
        val intent = Intent(this, SymptomReportActivity::class.java)
        startActivity(intent)
        finish()
    }
}
