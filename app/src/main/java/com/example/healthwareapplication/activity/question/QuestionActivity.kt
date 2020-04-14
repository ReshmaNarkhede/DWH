package com.example.healthwareapplication.activity.question

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionActivity : AppCompatActivity() {

    private lateinit var viewProductLayout: LinearLayout
    var allViewInstance: MutableList<View> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)

        viewProductLayout = findViewById(R.id.customOptionLL)
    }

    private fun defaultConfiguration() {

        val symptomJsonAry = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
        try {

            val ids: MutableList<String> = ArrayList()
            val array = JSONArray(symptomJsonAry)
            for (i in 0 until array.length()) {
                val model = SymptomJsonModel(array.getJSONObject(i))
                ids.add(model.getId()!!)
            }
            val idStr = android.text.TextUtils.join(",", ids)
            fetchQuestionData(idStr)
            Log.e("next: ", " $idStr")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun fetchQuestionData(idStr: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("symptoms_id", 4)
        AppHelper.printParam("QUESTION PAram:", param)

        val call: Call<JsonObject> = apiService.getQuestions(param)
        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("QUESTION URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("QUESTION REs:", response)

                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val questionAry = responseModel.getDataArray()!!
                        Log.e("QArySize: ", " " + questionAry.length())
                        setDynamicData(questionAry)
                    } else {
                        AppHelper.showToast(
                            this@QuestionActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@QuestionActivity)
                }
            }
        })
    }

    private fun setDynamicData(questionAry: JSONArray) {
        for (i in 0 until questionAry.length()) {
            val questionData = QuestionData(questionAry.getJSONObject(i))
            val customOptionsName = TextView(this)
            customOptionsName.textSize = 18f
            customOptionsName.setPadding(0, 15, 0, 15)
            customOptionsName.text = questionData.getQuestion()
            viewProductLayout.addView(customOptionsName)

            /***************************Radio*****************************************************/
            if (questionData.getQuestionType() == "SS") {
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = 3
                params.bottomMargin = 3
                val radioButtonJSONOpt = questionData.getAnswers()
                val rg = RadioGroup(this) //create the RadioGroup
                allViewInstance.add(rg)
                for (j in 0 until radioButtonJSONOpt!!.length()) {
                    val answerData = QuestionData.AnswerData(radioButtonJSONOpt.getJSONObject(j))
                    val rb = RadioButton(this)
                    rg.addView(rb, params)
                    if (j == 0) rb.isChecked = true
                    rb.layoutParams = params
                    rb.tag = answerData.getAnswerId()
                    rb.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    val ansStr = answerData.getAnswerValue()
                    rb.text = ansStr
                    rg.setOnCheckedChangeListener { group, checkedId ->
                        val radioButton = group.findViewById<View>(checkedId)
                        val variant_name = radioButton.tag.toString()
                        Toast.makeText(applicationContext, variant_name + "", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                viewProductLayout.addView(rg, params)
            }

//                    /***********************************CheckBox ***********************************************/
//            if (eachData.getString("option_type") == "C") {
//                val checkBoxJSONOpt = eachData.getJSONArray("variants")
//                for (j in 0 until checkBoxJSONOpt.length()) {
//                    if (!checkBoxJSONOpt.getJSONObject(j).getString("variant_name").equals("NO", ignoreCase = true)) {
//                        val chk = CheckBox(this)
//                        chk.setBackgroundColor(Color.parseColor("#FFFFFF"))
//                        allViewInstance.add(chk)
//                        chk.tag = checkBoxJSONOpt.getJSONObject(j).getString("variant_name")
//                        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                        params.topMargin = 3
//                        params.bottomMargin = 3
//                        val optionString = checkBoxJSONOpt.getJSONObject(j).getString("variant_name")
//                        chk.setOnClickListener { v ->
//                            val variant_name = v.tag.toString()
//                            Toast.makeText(applicationContext, variant_name + "", Toast.LENGTH_LONG).show()
//                        }
//                        chk.text = optionString
//                        viewProductLayout.addView(chk, params)
//                    }
//                }
//            }
//            if (eachData.getString("option_type") == "T") {
//                val til = TextInputLayout(this)
//                til.setHint("test hint")
//                val et = EditText(this)
//                til.addView(et)
//                allViewInstance.add(et)
//                viewProductLayout.addView(til)
//            }
        }
    }
}
