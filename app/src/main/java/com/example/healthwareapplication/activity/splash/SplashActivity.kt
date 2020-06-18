package com.example.healthwareapplication.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.LetsMeetActivity
import com.example.healthwareapplication.activity.dashboard.DashboardActivity
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.constants.AppConstants

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            showDashboard()
        }, AppConstants.k_SPLASH_TIME_OUT)
    }

    private fun showDashboard() {

        if (AppSessions.isSession(this)) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            val intent = Intent(this, LetsMeetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
