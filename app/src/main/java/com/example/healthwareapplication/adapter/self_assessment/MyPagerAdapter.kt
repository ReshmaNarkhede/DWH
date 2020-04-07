package com.example.healthwareapplication.adapter.self_assessment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Search"
            1 -> "Body"
            else -> {
                return "Third Tab"
            }
        }
    }
}