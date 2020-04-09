package com.example.healthwareapplication.activity.self_assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.MyPagerAdapter
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.fragment.self_assessment.BodyFragment
import com.example.healthwareapplication.fragment.self_assessment.SearchFragment
import com.google.android.material.tabs.TabLayout

class AddSymptomActivity : AppCompatActivity() {
    private lateinit var viewpager: ViewPager
    private lateinit var tabLayout: TabLayout

//    var bodyPartLists = arrayListOf<BodyParts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sa_detail)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        viewpager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabDots)

    }

    private fun defaultConfiguration() {
        setupViewPager()
    }
    private fun setupViewPager() {

        val adapter = MyPagerAdapter(supportFragmentManager)

        adapter.addFragment( SearchFragment())
        adapter.addFragment(BodyFragment())

        viewpager.adapter = adapter
        tabLayout.setupWithViewPager(viewpager)

        viewpager.addOnPageChangeListener(viewPagerPageChangeListener)
        viewpager.beginFakeDrag()
    }
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }
}
