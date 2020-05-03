package com.example.healthwareapplication.activity.self_assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
    }

    fun yesClick(view: View) {
        AppHelper.showToast(this,"Go to Current tab")
    }
    fun noClick(view: View) {
        AppHelper.showToast(this,"Go to Current tab")
    }
    fun notSureClick(view: View) {
        AppHelper.showToast(this,"Go to Current tab")
    }
}
