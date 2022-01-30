package com.example.healthwareapplication.activity.self_assessment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.databinding.ActivityDontKnowBinding
import org.json.JSONObject


class DontKnowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDontKnowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDontKnowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    fun cancelClick(view: View) {
        finish()
    }

    fun nxtQueClick(view: View) {
        binding.apply {
            val jsonObject = JSONObject()
            if (chkbox1.isChecked) {
                jsonObject.put("key1", chkbox1.text)
            } else {
                jsonObject.put("key1", "")
            }
            if (chkbox2.isChecked) {
                jsonObject.put("key2", chkbox2.text)
            } else {
                jsonObject.put("key2", "")
            }
            if (chkbox3.isChecked) {
                jsonObject.put("key3", chkbox3.text)
            } else {
                jsonObject.put("key3", "")
            }
            if (chkbox4.isChecked) {
                jsonObject.put("key4", chkbox4.text)
            } else {
                jsonObject.put("key4", "")
            }
            jsonObject.put("input", inputEdtTxt.text)

            Log.e("JSON: ", "" + jsonObject)
            intent = Intent()
            intent.putExtra("feedback", jsonObject.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
