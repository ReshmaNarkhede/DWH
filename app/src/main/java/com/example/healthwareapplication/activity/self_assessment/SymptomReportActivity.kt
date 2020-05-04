package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.AssessmentAdapter
import com.example.healthwareapplication.adapter.self_assessment.SelectedSymptomAdapter
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import org.json.JSONArray

class SymptomReportActivity : AppCompatActivity() {

    private lateinit var userNameTxt: TextView
    private lateinit var userGenderTxt: TextView
    private lateinit var userAgeTxt: TextView
    private lateinit var symptomTxt: TextView
    private lateinit var assesmentList: RecyclerView
    val delimiter = ","
    val finalStr = SpannableStringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dwh_symptom_report)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)

        userNameTxt = findViewById(R.id.userNameTxt)
        userGenderTxt = findViewById(R.id.userGenderTxt)
        userAgeTxt = findViewById(R.id.userAgeTxt)
        symptomTxt = findViewById(R.id.symptomTxt)
        assesmentList = findViewById(R.id.assesmentList)
    }

    private fun defaultConfiguration() {

        val ansStr = intent.getStringExtra(IntentConstants.kANSWER_DATA)
        val answerAry = JSONArray(ansStr)
        val symptomStr = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
        val symptmJsonAry = JSONArray(symptomStr)
        for (i in 0 until symptmJsonAry.length()) {
            val obj = SymptomJsonModel(symptmJsonAry.getJSONObject(i))
            finalStr.append(delimiter)
            finalStr.append(obj.getName())
        }
        Log.e("STr: ", ": " + finalStr.toString().replaceFirst(delimiter, ""))

        val user = AppSessions.getLoginModel(this)
        userNameTxt.text = user!!.firstName + " " + user.lastName
        userGenderTxt.text = user!!.sex

        val date = "30/2/2016".split("/")
//        val date = user!!.dob.split("/")
        userAgeTxt.text = AppHelper.getAge(date[2], date[1], date[0]) + "years"

        symptomTxt.text = finalStr.toString().replaceFirst(delimiter, "")

        assesmentList.layoutManager = LinearLayoutManager(this)
        val addAdapter = AssessmentAdapter(answerAry!!)
        assesmentList.adapter = addAdapter
    }

    fun okClick(view: View) {
        val intent = Intent(this, FeedbackActivity::class.java)
        startActivity(intent)
        finish()
    }
}
