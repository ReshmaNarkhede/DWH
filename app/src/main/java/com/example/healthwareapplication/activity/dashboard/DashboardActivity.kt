package com.example.healthwareapplication.activity.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.LetsMeetActivity
import com.example.healthwareapplication.activity.account.register.RegisterAsActivity
import com.example.healthwareapplication.activity.self_assessment.SAHomeActivity
import com.example.healthwareapplication.adapter.home.HomeMenuAdapter
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.menu.MenuModel
import java.util.ArrayList

class DashboardActivity : AppCompatActivity(), RecyclerItemClickListener.OnItemClickListener {
    private var menuAry: ArrayList<MenuModel> = ArrayList()
    private lateinit var menuList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initComponents()
        defaultConfigurations()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        menuList = findViewById(R.id.menuList)

    }

    private fun defaultConfigurations() {
        menuAry = loadMenu()
        setAdapter()
    }

    fun loadMenu(): ArrayList<MenuModel> {
        val user = AppSessions.getLoginModel(this)

        val menuList = ArrayList<MenuModel>()
        menuList.add(MenuModel("1", "Family", R.drawable.ic_family, R.drawable.t1))
        menuList.add(MenuModel("2", "Emergency", R.drawable.ic_emergency, R.drawable.t2))
        menuList.add(MenuModel("3", "Drug", R.drawable.ic_drug, R.drawable.t3))
        if (user!!.userType == 3) {
            menuList.add(MenuModel("4", "Doctor Patient", R.drawable.ic_patient, R.drawable.t4))
        }
        menuList.add(MenuModel("5", "Self Assessment", R.drawable.ic_self_assisment, R.drawable.t4))
        menuList.add(MenuModel("6", "My Personal Health Record", R.drawable.ic_logo, R.drawable.t5))
        menuList.add(MenuModel("7", "Coming Soon", R.drawable.ic_logo, R.drawable.t6))
        menuList.add(MenuModel("8", "Coming Soon", R.drawable.ic_logo, R.drawable.t7))
        menuList.add(MenuModel("9", "Log Out", R.drawable.ic_logout, R.drawable.t8))

        return menuList
    }

    private fun setAdapter() {
        menuList.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        val adapter = HomeMenuAdapter(menuAry, this)
        menuList.adapter = adapter
    }



    override fun onItemClick(view: View?, position: Int) {
        val menuModel = menuAry[position]
        when (menuModel.id) {
            "1" -> {
            }
            "2" -> {
            }
            "3" -> {
            }
            "4" -> {
                //For Dr Doctor Patient module
            }
            "5" -> {
                val intent = Intent(this,SAHomeActivity::class.java)
                startActivity(intent)
            }
            "6" -> {
            }
            "9" -> {
                logout()
            }
        }
    }
    private fun logout() {
        AppSettings.clearAllData(this)
        val intent = Intent(this, LetsMeetActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun backClick(view: View) {
        finishAffinity()
    }
}
