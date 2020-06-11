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
import com.example.healthwareapplication.adapter.self_assessment.question.RadioRecyclerViewAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_question.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionDemoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var symptmJsonAry: JSONArray
    private var ansJsonObj: JSONObject? = JSONObject()
    private var ansJsonAry: JSONArray? = JSONArray()

    private var questionAry: JSONArray = JSONArray()
    var innerIndex: Int? = 0
    val delimiter = ","
    val finalStr = SpannableStringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        radioList.isNestedScrollingEnabled = false
    }

    private fun defaultConfiguration() {

        val symptomStr = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
        symptmJsonAry = JSONArray(symptomStr)
        for (i in 0 until symptmJsonAry.length()) {
            val obj = SymptomJsonModel(symptmJsonAry.getJSONObject(i))
            Log.e("ID: ", ": " + obj.getId())
            finalStr.append(delimiter)
            finalStr.append(obj.getId())
        }
        val searchStr = finalStr.toString().replaceFirst(delimiter, "")
        Log.e("STr: ", ": $searchStr")
        fetchQuestionData(searchStr)

        setDateAnswer()
    }

    private fun setDateAnswer() {
        val assessmentDate = AppSettings.getStringValue(this, IntentConstants.kASSESSMENT_DATE)
        val assessmentTime = AppSettings.getStringValue(this, IntentConstants.kASSESSMENT_TIME)

        answerTxt.text = "$assessmentDate, $assessmentTime"
        answerTxt.setOnClickListener(this)
    }

    private fun fetchQuestionData(idStr: String) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("symptoms_id", idStr)
        AppHelper.printParam("QUESTION PAram:", param)

        val call: Call<JsonObject> = apiService.getQuestions(param)
//        DialogUtility.showProgressDialog(this)
        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                AppHelper.printUrl("QUESTION URL:", response)

                if (response.isSuccessful) {
                    AppHelper.printResponse("QUESTION REs:", response)

//                    DialogUtility.hideProgressDialog()
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val tempAry = responseModel.getDataArray()
                        AppSettings.setJsonArrayValue(
                            this@QuestionDemoActivity,
                            AppConstants.kQUESTION_ARY,
                            tempAry.toString()
                        )
                        getAllQuestion(tempAry)
                    } else {
                        questionTxt.text = "No question for this Symptom."
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    DialogUtility.hideProgressDialog()
                    AppHelper.showNetNotAvailable(this@QuestionDemoActivity)
                }
            }
        })
    }

    private fun getAllQuestion(tempAry: JSONArray?) {

        for (i in 0 until tempAry!!.length()) {
            val obj = QuestionData(tempAry.getJSONObject(i))
            for (j in 0 until obj.getQuestionAns()!!.length()) {
                val ansObj = obj.getQuestionAns()!!.getJSONObject(j)
                questionAry.put(ansObj)
            }
        }
        Log.e("for: ", " ${questionAry.length()}")
        setDynamicData(innerIndex,ansJsonObj!!)
    }
    private fun setDynamicData(innerIndex: Int?, ansJObj: JSONObject) {
        Log.e("index: ", " : $innerIndex")
        ansJsonObj = ansJObj
        val ansObj = QuestionData.QuestionAnsModel(ansJsonObj!!)
        if (innerIndex!! > 0) {
            answerTxt.text = ansObj.getSelectedAnswer()!!.toLowerCase().split(' ')
                .joinToString(" ") { it.capitalize() }
        } else {
            setDateAnswer()
        }

        val qObj = QuestionData.QuestionAnsModel(questionAry.getJSONObject(innerIndex))
        questionTxt.text = qObj.getQuestion()

        if (qObj.getQuestionType() == "SS") //radio button
        {
            showRadioData(questionAry.getJSONObject(innerIndex))
        }
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.answerTxt -> {
                if (innerIndex!! < questionAry!!.length() && innerIndex!! > 0) {
                    innerIndex = innerIndex!!.minus(1)
                    if (innerIndex!! > 0) {
                        ansJsonObj = ansJsonAry!!.getJSONObject(innerIndex!! - 1)
                    }
                    ansJsonAry!!.remove(innerIndex!!)
                    setDynamicData(innerIndex, ansJsonObj!!)
                } else {
                    finish()
                }
            }
        }
    }

    private fun showRadioData(qObj: JSONObject) {
        val qModel = QuestionData.QuestionAnsModel(qObj)
        val recyclerLayoutManager = LinearLayoutManager(this)
        radioList.layoutManager = recyclerLayoutManager
        val result: List<String> = qModel.getAnswerOptionsList()
        val radioRecyclerAdapter = RadioRecyclerViewAdapter(
            result,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val ansObj = result[position].replace("\\'", "'")
                qObj!!.put("selected_answer", ansObj)

                if (ansObj.contains("Don't know")) {

                    val intent = Intent(this, DontKnowActivity::class.java)
                    startActivityForResult(intent, 201)

                } else if (qModel.getAnswer()!!.equals("any", true)) {
                    if (innerIndex!! < (questionAry!!.length() - 1)) {
                        innerIndex = innerIndex!!.plus(1)
                        setDynamicData(innerIndex, qObj)
                    } else {
                            Log.e("Ans Ary Size:", ": " + ansJsonAry!!.length())
                            val intent = Intent(this, ThankYouActivity::class.java)
                            intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptmJsonAry.toString())
                            intent.putExtra(IntentConstants.kANSWER_DATA, ansJsonAry.toString())
                            startActivity(intent)
                    }
                    ansJsonAry!!.put(qObj)
                }
            })
        radioList.adapter = radioRecyclerAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            if (resultCode == RESULT_OK) {

                val feedbackStr = data!!.extras!!.getString("feedback")
                val dntKnowObj = JSONObject(feedbackStr!!)
                val qObj = questionAry.getJSONObject(innerIndex!!)
                qObj.put("dont_know_feedback", dntKnowObj)
                ansJsonAry!!.put(qObj)

                if (innerIndex!! < (questionAry.length() - 1)) {
                    innerIndex = innerIndex!!.plus(1)
                    setDynamicData(innerIndex, qObj)
                }
            }
        }
    }

    override fun onBackPressed() {

        if (innerIndex == 0) {
            super.onBackPressed()
        }
        if (innerIndex!! < questionAry!!.length() && innerIndex!! > 0) {
            innerIndex = innerIndex!!.minus(1)
            if (innerIndex!! > 0) {
                ansJsonObj = ansJsonAry!!.getJSONObject(innerIndex!! - 1)
            }
            ansJsonAry!!.remove(innerIndex!!)
            setDynamicData(innerIndex, ansJsonObj!!)
        }
    }
}
