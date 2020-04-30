package com.example.healthwareapplication.activity.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.fragment.home.HomeFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initComponents()
        defaultConfigurations()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        frameLayout = findViewById(R.id.containerLayout)
        val fragment = HomeFragment()
        addFragment(fragment)
    }

    private fun defaultConfigurations() {
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.containerLayout, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    fun backClick(view: View) {
       onBackPressed()
    }
}
