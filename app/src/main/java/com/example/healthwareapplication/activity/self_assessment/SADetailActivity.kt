package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.question.QuestionActivity
import com.example.healthwareapplication.app_utils.AppHelper

class SADetailActivity : AppCompatActivity() {
    private lateinit var symptom:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_s_a_detail)

        initComponents()
    }
    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        symptom = findViewById(R.id.symptom)
    }

    fun addSymptom(view: View) {
        val intent = Intent(this, AddSymptomActivity::class.java)
//        startActivity(intent)
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if(data!=null) {
                val message = data!!.getStringExtra("MESSAGE")
                symptom.text = message
                symptom.setBackgroundColor(resources.getColor(R.color.body_outline))
                val img = resources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel)
                symptom.setCompoundDrawables(null, null, img, null)
            }
        }
    }

    fun clickNext(view: View) {
        val intent = Intent(this, QuestionActivity::class.java)
        startActivity(intent)
    }
}
