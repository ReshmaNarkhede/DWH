package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.AssessmentAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.SAListModel
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dwh_symptom_report.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportFromHome : AppCompatActivity() {

    val delimiter = "-"
    val finalStr = SpannableStringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dwh_symptom_report)


        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        getReportAPI()
    }

    private fun defaultConfiguration() {

        val reportStr = intent.getStringExtra(IntentConstants.REPORT_DATA)
        val reportObj = JSONObject(reportStr!!)
        val model = SAListModel(reportObj)
        Log.e("STr: ", ": $reportStr")

        userNameTxt.text = AppSessions.getUserName(this)
        userGenderTxt.text = AppSessions.getUserSex(this)


        userAgeTxt.text = AppSessions.getUserAge(this)

        symptomTxt.text = model.getSymptom()

//        assesmentList.layoutManager = LinearLayoutManager(this)
//        val addAdapter = AssessmentAdapter(answerAry!!)
//        assesmentList.adapter = addAdapter
    }

    private fun getReportAPI() {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("user_id", AppSessions.getUserId(this))

        AppHelper.printParam("report PAram:", param)

        val call: Call<JsonObject> = apiService.getReport(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("report URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("report REs:", response)

                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString() )
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val reportAry = responseModel.getDataArray()
                        bindList(reportAry)
                    } else {
                        AppHelper.showToast(
                            this@ReportFromHome,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@ReportFromHome)
                }
            }
        })
    }

    private fun bindList(reportAry: JSONArray?) {
        assesmentList.layoutManager = LinearLayoutManager(this)
        val addAdapter = AssessmentAdapter(reportAry!!)
        assesmentList.adapter = addAdapter
    }

    fun okClick(view: View) {
        finish()
    }
}