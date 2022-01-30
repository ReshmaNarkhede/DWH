package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun yesClick(view: View) {
        openNEW()
    }

    fun noClick(view: View) {
        openNEW()
    }

    fun notSureClick(view: View) {
        openNEW()
    }

    private fun openNEW() {
        val intent = Intent(this,SAHomeActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun reportClick(view: View) {
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
