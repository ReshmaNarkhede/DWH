package com.example.healthwareapplication.activity.question

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionActivity : AppCompatActivity() {

    //    private lateinit var viewProductLayout: LinearLayout
    private lateinit var questionView: LinearLayout
//    var allViewInstance: MutableList<View> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)

        questionView = findViewById(R.id.questionView)
//        viewProductLayout = findViewById(R.id.customOptionLL)
    }

    private fun defaultConfiguration() {
        fetchQuestionData("4")
//        val symptomJsonAry = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
//        try {
//
//            val ids: MutableList<String> = ArrayList()
//            val array = JSONArray(symptomJsonAry)
//            for (i in 0 until array.length()) {
//                val model = SymptomJsonModel(array.getJSONObject(i))
//                ids.add(model.getId()!!)
//            }
//            val idStr = android.text.TextUtils.join(",", ids)
//            fetchQuestionData(idStr)
//            Log.e("next: ", " $idStr")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
    }

    private fun fetchQuestionData(idStr: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("symptoms_id", idStr)
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
            val view: View = getQuestionView(questionData)
            questionView.addView(view)
        }
    }

    private fun getQuestionView(questionData: QuestionData): View {
        var view: View? = null
        if (questionData.getQuestionType() == "SS") // yes no option
        {
            view = layoutInflater.inflate(R.layout.survay_radio_button_view, null)
            val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
            val question = view.findViewById<TextView>(R.id.question)
            val comment = view.findViewById<EditText>(R.id.comment)
            radioGroup.removeAllViews()
            question.text = questionData.getQuestion()
            val answerArray = questionData.getAnswers()
            for (j in 0 until answerArray!!.length()) {
                val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
                val radio =
                    layoutInflater.inflate(R.layout.survay_radio_button, null) as RadioButton
                radio.id = j + 1001
                radio.text = answerData.getAnswerValue()
                radio.tag = answerData.getAnswerId()
                radioGroup.addView(radio)
            }
        }
        if (questionData.getQuestionType() == "CB") // CrossBar
        {
            view = layoutInflater.inflate(R.layout.survay_radio_button_view, null)
            val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
            val question = view.findViewById<TextView>(R.id.question)
            radioGroup.removeAllViews()
            question.text = questionData.getQuestion()
            val answerArray = questionData.getAnswers()
            for (j in 0 until answerArray!!.length()) {
                val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
                val radio = layoutInflater.inflate(R.layout.survay_radio_button, null) as RadioButton
                radio.id = j + 1001
                radio.text = answerData.getAnswerValue()
                radio.tag = answerData.getAnswerId()
                radioGroup.addView(radio)
            }
        }
        if (questionData.getQuestionType() == "MS") // Checkbox
        {
            view = layoutInflater.inflate(R.layout.survay_check_box_view, null)
            val checkboxGroup = view.findViewById<LinearLayout>(R.id.checkboxGroup)
            val question = view.findViewById<TextView>(R.id.question)
            question.text = questionData.getQuestion()
            val answerArray = questionData.getAnswers()
            for (j in 0 until answerArray!!.length()) {
                val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
                val checkbox = layoutInflater.inflate(R.layout.survay_check_box, null) as CheckBox
                checkbox.id = j + 1001
                checkbox.text = answerData.getAnswerValue()
                checkbox.tag = answerData.getAnswerId()
                checkboxGroup.addView(checkbox)
            }
        }
        return view!!
    }
}
