package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthwareapplication.R.layout.dwh_symptom_report
import com.example.healthwareapplication.adapter.self_assessment.AssessmentAdapter
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import kotlinx.android.synthetic.main.dwh_symptom_report.*
import org.json.JSONArray

class SymptomReportActivity : AppCompatActivity() {

    val delimiter = "-"
    val finalStr = SpannableStringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dwh_symptom_report)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)

        okayBtn.text = "Done"
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

        userNameTxt.text = AppSessions.getUserName(this)
        userGenderTxt.text = AppSessions.getUserSex(this)


        userAgeTxt.text = AppSessions.getUserAge(this)

        symptomTxt.text = finalStr.toString().replaceFirst(delimiter, "")

        assesmentList.layoutManager = LinearLayoutManager(this)
        val addAdapter = AssessmentAdapter(answerAry!!)
        assesmentList.adapter = addAdapter
    }

    fun okClick(view: View) {
        val intent = Intent(this, FeedbackActivity::class.java)
        startActivity(intent)
    }
}
