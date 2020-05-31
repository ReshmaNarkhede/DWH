package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthwareapplication.R.layout.activity_what_feel_detail
import com.example.healthwareapplication.adapter.self_assessment.SelectedSymptomAdapter
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_what_feel_detail.*
import org.json.JSONArray
import org.json.JSONObject

class WhatFeelActivity : AppCompatActivity() {
    private lateinit var gson: Gson
    val symptmJsonAry: JSONArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_what_feel_detail)

        initComponents()
    }

    private fun initComponents() {

        AppHelper.transparentStatusBar(this)
        gson = Gson()
        dataBind()

    }

    fun addSymptom(view: View) {
        val intent = Intent(this, BodySymptomActivity::class.java)
        startActivityForResult(intent, 2)
    }

    fun searchClick(view: View) {
        val intent = Intent(this, SearchSymptomActivity::class.java)
        startActivityForResult(intent, 3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (data != null) {
                val modelObj = data!!.getStringExtra(IntentConstants.kSYMPTOM_SELECTED)
                addToList(modelObj)
            }
        }
        if (requestCode == 3) {
            if (data != null) {
                val modelObj = data!!.getStringExtra(IntentConstants.kSYMPTOM_SELECTED)
                addToList(modelObj)
            }
        }
    }

    private fun addToList(modelObj: String?) {
        var isAllow:Boolean = true
        if (symptmJsonAry.length() > 3) {
            AppHelper.showToast(this, "You are not able to add more symptom")
        } else {
            if (symptmJsonAry.length() > 0) {
                val obj = SymptomJsonModel(JSONObject(modelObj!!))
                for (i in 0 until symptmJsonAry.length()) {
                    val selectedSymptmObj = SymptomJsonModel(symptmJsonAry.getJSONObject(i))
                    if (selectedSymptmObj.getName().equals(obj.getName(), true)) {
                        AppHelper.showToast(this, "You are not able to add same symptom again")
                        isAllow = false
                    }
                }
            }
            if(isAllow) {
                symptmJsonAry.put(JSONObject(modelObj!!))
                symptomList.adapter!!.notifyDataSetChanged()
                showBottom()
            }
        }
    }

    private fun dataBind() {
        val showDeleted: ShowDeleted = object : ShowDeleted {
            override fun showDeleted(size: Int) {
                if (size == 0) {
                    nextBtn.visibility = View.GONE
                }
            }
        }
        symptomList.layoutManager = LinearLayoutManager(this)
        val addAdapter = SelectedSymptomAdapter(symptmJsonAry!!, showDeleted)
        symptomList.adapter = addAdapter
    }

    private fun showBottom() {
        nextBtn.visibility = View.VISIBLE
    }


    fun clickNext(view: View) {
        val intent = Intent(this, WhenStartActivity::class.java)
        intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptmJsonAry.toString())
        startActivity(intent)
    }

    interface ShowDeleted {
        fun showDeleted(size: Int)
    }

    fun backClick(view: View) {
        finish()
    }
}
