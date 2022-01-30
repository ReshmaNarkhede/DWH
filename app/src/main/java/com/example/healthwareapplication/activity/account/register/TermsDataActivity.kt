package com.example.healthwareapplication.activity.account.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityTermsDataBinding

class TermsDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        val label = intent.getStringExtra(IntentConstants.kLABEL)
        binding.termsLabel.text = label
    }

    fun backClick(view: View) {
        finish()
    }
}