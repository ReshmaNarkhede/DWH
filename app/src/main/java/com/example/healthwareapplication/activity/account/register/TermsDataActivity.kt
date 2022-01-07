package com.example.healthwareapplication.activity.account.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_terms_data
import com.example.healthwareapplication.constants.IntentConstants
import kotlinx.android.synthetic.main.activity_terms_data.*

class TermsDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_terms_data)

        initComponents()
    }

    private fun initComponents() {
        val label = intent.getStringExtra(IntentConstants.kLABEL)
        termsLabel.text = label
    }

    fun backClick(view: View) {
        finish()
    }
}