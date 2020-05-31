package com.example.healthwareapplication.activity.self_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_thank_you
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_thank_you.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThankYouActivity : AppCompatActivity() {

    private var symptomStr: String? = null
    private var ansStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_thank_you)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfiguration() {
        symptomStr = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
        ansStr = intent.getStringExtra(IntentConstants.kANSWER_DATA)
        val user = AppSessions.getLoginModel(this)
        thankYouText.text = getString(R.string.thank_you_after_report,user!!.firstName)
        Log.e("Ans: ", ":: $ansStr")
        submitReport(ansStr)
    }

    fun okClick(view: View) {
        val intent = Intent(this, SymptomReportActivity::class.java)
        intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptomStr)
        intent.putExtra(IntentConstants.kANSWER_DATA,ansStr)
        startActivity(intent)
    }


    private fun submitReport(ansStr: String?) {
        val assessmentDate = AppSettings.getStringValue(this,IntentConstants.kASSESSMENT_DATE)
        val assessmentTime = AppSettings.getStringValue(this,IntentConstants.kASSESSMENT_TIME)

        val jsonArray = JSONArray(ansStr)
        val jsonParser = JsonParser()
        val report = jsonParser.parse(jsonArray.toString()) as JsonArray

        val apiService: ApiInterface = ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("user_id", AppSessions.getUserId(this))
        param.addProperty("assessment_date", assessmentDate)
        param.addProperty("assessment_time", assessmentTime)
        param.add("sefl_assessment_test", report)

        AppHelper.printParam("submit PAram:", param)

        val call: Call<JsonObject> = apiService.submitSelfData(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("submit URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("submit REs:", response)

                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        AppHelper.showToast(this@ThankYouActivity,responseModel.getMessage().toString())
                    } else {
                        AppHelper.showToast(
                            this@ThankYouActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@ThankYouActivity)
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        finish()
        val intent = Intent(this,SAHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
