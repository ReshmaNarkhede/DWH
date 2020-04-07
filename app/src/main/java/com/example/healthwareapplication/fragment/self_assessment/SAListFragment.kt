package com.example.healthwareapplication.fragment.self_assessment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.login.HaveWeMetActivity
import com.example.healthwareapplication.activity.self_assessment.SADetailActivity
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions

class SAListFragment : Fragment(), View.OnClickListener {
    private lateinit var addBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.sa_fragment_list, container, false)
        initComponents(view)
        defaultConfiguration()
        return view
    }

    private fun initComponents(view: View?) {

        addBtn = view!!.findViewById(R.id.addBtn)
    }

    private fun defaultConfiguration() {
        addBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addBtn -> {
                val intent = Intent(activity, SADetailActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
