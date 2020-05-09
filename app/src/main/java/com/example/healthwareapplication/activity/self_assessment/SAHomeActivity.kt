package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.dashboard.DashboardActivity

class SAHomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var addBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_a_home)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        addBtn = findViewById(R.id.addBtn)
    }

    private fun defaultConfiguration() {
        addBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addBtn -> {
                val intent = Intent(this, SADetailActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun backClick(view: View) {
        val intent = Intent(this,DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
