package com.example.healthwareapplication.activity.self_assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R

class DontKnowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dont_know)
    }

    fun cancelClick(view: View) {
        finish()
    }
}
