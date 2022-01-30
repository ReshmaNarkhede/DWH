package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthwareapplication.adapter.self_assessment.AssessmentAdapter
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.DwhSymptomReportBinding
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import org.json.JSONArray

class SymptomReportActivity : AppCompatActivity() {

    private lateinit var binding: DwhSymptomReportBinding
    val delimiter = "-"
    val finalStr = SpannableStringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DwhSymptomReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)

        binding.okayBtn.text = "Done"
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
        binding.userNameTxt.text = AppSessions.getUserName(this)
        binding.userGenderTxt.text = AppSessions.getUserSex(this)


        binding.userAgeTxt.text = AppSessions.getUserAge(this)

        binding.symptomTxt.text = finalStr.toString().replaceFirst(delimiter, "")

        binding.assesmentList.layoutManager = LinearLayoutManager(this)
        val addAdapter = AssessmentAdapter(answerAry, false)
        binding.assesmentList.adapter = addAdapter

    }

    fun okClick(view: View) {
        val intent = Intent(this, FeedbackActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,SAHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
