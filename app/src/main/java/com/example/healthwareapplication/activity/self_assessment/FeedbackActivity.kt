package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
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
        finish()
    }
}
