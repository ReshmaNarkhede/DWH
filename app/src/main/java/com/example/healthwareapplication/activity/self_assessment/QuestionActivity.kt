package com.example.healthwareapplication.activity.self_assessment

import android.R.id
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    //    private lateinit var dntKnowObj: JSONObject
    private lateinit var symptmJsonAry: JSONArray
    private var ansJsonObj: JSONObject? = JSONObject()
    private var ansJsonAry: JSONArray? = JSONArray()
    private var QArray: JSONArray? = JSONArray()

    private lateinit var dataAry: JSONArray
    var outerIndex: Int? = 0
    var innerIndex: Int? = 0
    private lateinit var questionTxt: TextView
    private lateinit var answerTxt: TextView
    private lateinit var radioList: RecyclerView

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
        questionTxt = findViewById(R.id.questionTxt)
        answerTxt = findViewById(R.id.answerTxt)
        radioList = findViewById(R.id.radioList)
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
        Log.e("STr: ", ": " + finalStr.toString().replaceFirst(delimiter, ""))
        dataAry = AppSessions.getQuestionData(this)!!
        if (dataAry.length() == 0) {
            fetchQuestionData("6")
        } else {
            setOuterLoop(outerIndex)
        }
        answerTxt.setOnClickListener(this)
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
                        dataAry = responseModel.getDataArray()!!

                        AppSettings.setJsonArrayValue(
                            this@QuestionActivity,
                            AppConstants.kQUESTION_ARY,
                            dataAry.toString()
                        )
                        setOuterLoop(outerIndex)
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

    private fun setOuterLoop(outerIndex: Int?) {
        QArray = QuestionData(dataAry.getJSONObject(outerIndex!!)).getQuestionAns()
        setDynamicData(innerIndex, ansJsonObj!!)
    }

    private fun setDynamicData(index: Int?, ansJObj: JSONObject) {

        ansJsonObj = ansJObj
        val ansObj = QuestionData.QuestionAnsModel(ansJsonObj!!)

        val qObj = QuestionData.QuestionAnsModel(QArray!!.getJSONObject(index!!))

        answerTxt.text = ansObj.getSelectedAnswer()
        questionTxt.text = qObj.getQuestion()

        if (qObj.getQuestionType() == "SS") //radio button
        {
            showRadioData(QArray!!.getJSONObject(index!!))
        }
    }

    private fun showRadioData(qObj: JSONObject) {
        val qModel = QuestionData.QuestionAnsModel(qObj)
        val recyclerLayoutManager = LinearLayoutManager(this)
        radioList.layoutManager = recyclerLayoutManager
        val str = qModel.getAnswerOptions()
        val result: List<String> = str!!.split("#")
        val radioRecyclerAdapter = RadioRecyclerViewAdapter(
            result,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val ansObj = result[position]
                qObj!!.put("selected_answer", ansObj)

                if (ansObj.contains("know")) {

                    val intent = Intent(this, DontKnowActivity::class.java)
                    startActivityForResult(intent, 201)

                } else if (qModel.getAnswer() == "any") {
                    Log.e("RadioAns: $innerIndex", ": $ansObj")
                    if (innerIndex!! < (QArray!!.length() - 1)) {
                        innerIndex = innerIndex!!.plus(1)
                        setDynamicData(innerIndex, qObj)
                    } else {
                        Log.e("Ans Ary Size:", ": " + ansJsonAry!!.length())
                        val intent = Intent(this, ThankYouActivity::class.java)
                        intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptmJsonAry.toString())
                        intent.putExtra(IntentConstants.kANSWER_DATA, ansJsonAry.toString())
                        startActivity(intent)
                        finish()
                    }
                    ansJsonAry!!.put(qObj)
                }
            })
        radioList.adapter = radioRecyclerAdapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.answerTxt -> {
                if (innerIndex!! < QArray!!.length() && innerIndex!! > 0) {
                    innerIndex = innerIndex!!.minus(1)
                    Log.e("back: ", ": $innerIndex")
                    ansJsonObj = JSONObject()
                    ansJsonAry!!.remove(innerIndex!!)
                    setDynamicData(innerIndex, ansJsonObj!!)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            if (resultCode == RESULT_OK) {

                val feedbackStr = data!!.extras!!.getString("feedback")
                val dntKnowObj = JSONObject(feedbackStr!!)
                val qObj = QArray!!.getJSONObject(innerIndex!!)
                qObj.put("dont_know_feedback", dntKnowObj)
                ansJsonAry!!.put(qObj)

                if (innerIndex!! < (QArray!!.length() - 1)) {

                     innerIndex = innerIndex!!.plus(1)
                    setDynamicData(innerIndex, qObj)
                }
            }
        }
    }
}
