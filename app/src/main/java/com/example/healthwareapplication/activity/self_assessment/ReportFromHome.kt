package com.example.healthwareapplication.activity.self_assessment

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.adapter.self_assessment.AssessmentAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.DwhSymptomReportBinding
import com.example.healthwareapplication.model.self_assessment.SAListModel
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportFromHome : AppCompatActivity() {

    private lateinit var binding: DwhSymptomReportBinding
    val delimiter = "-"
    val finalStr = SpannableStringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DwhSymptomReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)

        binding.okayBtn.text = "Back"

        val reportStr = intent.getStringExtra(IntentConstants.REPORT_DATA)
        val reportObj = JSONObject(reportStr!!)
        val model = SAListModel(reportObj)
        getReportAPI(model)

        defaultConfiguration(model)
    }

    private fun defaultConfiguration(model: SAListModel) {

        binding.userNameTxt.text = AppSessions.getUserName(this)
        binding.userGenderTxt.text = AppSessions.getUserSex(this)
        binding.userAgeTxt.text = AppSessions.getUserAge(this)
        binding.symptomTxt.text = model.getSymptom()

//        assesmentList.layoutManager = LinearLayoutManager(this)
//        val addAdapter = AssessmentAdapter(answerAry!!)
//        assesmentList.adapter = addAdapter
    }

    private fun getReportAPI(model: SAListModel) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("symptom_id", model.getSymptomId())
        param.addProperty("ass_report_id", model.getId())

        AppHelper.printParam("report PAram:", param)

        val call: Call<JsonObject> = apiService.getReport(param)
//        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("report URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("report REs:", response)

//                    DialogUtility.hideProgressDialog()
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
//                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@ReportFromHome)
                }
            }
        })
    }

    private fun bindList(reportAry: JSONArray?) {
        binding.assesmentList.layoutManager = LinearLayoutManager(this)
        val addAdapter = AssessmentAdapter(reportAry!!,true)
        binding.assesmentList.adapter = addAdapter
    }

    fun okClick(view: View) {
        finish()
    }
}