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
import com.example.healthwareapplication.R.layout.activity_dont_know
import com.example.healthwareapplication.app_utils.AppHelper
import kotlinx.android.synthetic.main.activity_dont_know.*
import org.json.JSONObject


class DontKnowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_dont_know)

        initComponents()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
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
