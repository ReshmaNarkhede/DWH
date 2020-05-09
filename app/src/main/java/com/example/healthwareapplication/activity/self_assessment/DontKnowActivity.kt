package com.example.healthwareapplication.activity.self_assessment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import org.json.JSONObject


class DontKnowActivity : AppCompatActivity() {
    private lateinit var chkbox1: CheckBox
    private lateinit var chkbox2: CheckBox
    private lateinit var chkbox3: CheckBox
    private lateinit var chkbox4: CheckBox
    private lateinit var inputEdtTxt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dont_know)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        chkbox1 = findViewById(R.id.chkbox1)
        chkbox2 = findViewById(R.id.chkbox2)
        chkbox3 = findViewById(R.id.chkbox3)
        chkbox4 = findViewById(R.id.chkbox4)
        inputEdtTxt = findViewById(R.id.inputEdtTxt)
    }

    private fun defaultConfiguration() {

    }

    fun cancelClick(view: View) {
        finish()
    }

    fun nxtQueClick(view: View) {
        val jsonObject = JSONObject()
        if(chkbox1.isChecked){
            jsonObject.put("key1",chkbox1.text)
        }else{
            jsonObject.put("key1","")
        }
        if(chkbox2.isChecked){
            jsonObject.put("key2",chkbox2.text)
        }else{
            jsonObject.put("key2","")
        }
        if(chkbox3.isChecked){
            jsonObject.put("key3",chkbox3.text)
        }else{
            jsonObject.put("key3","")
        }
        if(chkbox4.isChecked){
            jsonObject.put("key4",chkbox4.text)
        }else{
            jsonObject.put("key4","")
        }
        jsonObject.put("input",inputEdtTxt.text)

        Log.e("JSON: ","" + jsonObject)
        intent = Intent()
        intent.putExtra("feedback", jsonObject.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
