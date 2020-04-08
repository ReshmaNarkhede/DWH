package com.example.healthwareapplication.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.login.LoginAsActivity
import com.example.healthwareapplication.adapter.home.HomeMenuAdapter
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.fragment.self_assessment.SAListFragment
import com.example.healthwareapplication.model.menu.MenuModel
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.Gson
import java.util.ArrayList

class HomeFragment : Fragment(), RecyclerItemClickListener.OnItemClickListener {

    private lateinit var gson: Gson
    private lateinit var menuList: RecyclerView
    private var menuAry: ArrayList<MenuModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_home, container, false)
        initComponents(view!!)
        defaultConfiguration()
        return view
    }

    private fun initComponents(view: View) {

        gson = Gson()
        menuList = view.findViewById(R.id.menuRecyclerView)
    }

    private fun defaultConfiguration() {
        menuAry = loadMenu()
        setAdapter()
    }

    fun loadMenu(): ArrayList<MenuModel> {
        val user = AppSessions.getLoginModel(context!!)

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
                addFragment(SAListFragment())
            }
            "6" -> {
            }
            "9" -> {
                logout()
            }
        }
    }
    private fun addFragment(fragment: Fragment) {
        fragmentManager!!
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.containerLayout, fragment, fragment.javaClass.simpleName)
            .commit()
    }
    private fun logout() {
        AppSettings.clearMyPreference(activity!!)
        val intent = Intent(activity, LoginAsActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }
}
