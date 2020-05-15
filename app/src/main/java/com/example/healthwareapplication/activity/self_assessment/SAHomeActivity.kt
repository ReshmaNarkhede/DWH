package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.dashboard.DashboardActivity
import com.example.healthwareapplication.app_utils.AppHelper

class SAHomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var addImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_a_home)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        addImg = findViewById(R.id.addImg)
    }

    private fun defaultConfiguration() {
        addImg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addImg -> {
                val intent = Intent(this, SADetailActivity::class.java)
                startActivity(intent)
            }
        }
    }
//    fun backClick(view: View) {
//        val intent = Intent(this,DashboardActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}
