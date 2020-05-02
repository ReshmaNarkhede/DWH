package com.example.healthwareapplication.activity.question

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.question.CheckRecyclerViewAdapter
import com.example.healthwareapplication.adapter.self_assessment.question.RadioRecyclerViewAdapter
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.google.gson.JsonObject
import com.warkiz.widget.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var ansJsonObj: JSONObject? = JSONObject()
    private var QArray: JSONArray? = JSONArray()

    private lateinit var dataAry: JSONArray
    var outerIndex: Int? = 0
    var innerIndex: Int? = 0
    private lateinit var questionTxt: TextView
    private lateinit var answerTxt: TextView
    private lateinit var radioList: RecyclerView

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

//        dataAry = AppSessions.getQuestionData(this)!!
//        if (dataAry.length() == 0) {
        fetchQuestionData("2")
//        } else {
//            setOuterLoop(outerIndex)
//        }
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
        setDynamicData(innerIndex,ansJsonObj)
    }
    private fun setDynamicData(index: Int?, ansJObj: JSONObject?) {

        ansJsonObj = ansJObj

        val ansObj = QuestionData.QuestionAnsModel(ansJsonObj!!)

        val qObj = QuestionData.QuestionAnsModel(QArray!!.getJSONObject(index!!))

        answerTxt.text = ansObj.getSelectedAnswer()
        questionTxt.text = qObj.getQuestion()

        if (qObj.getQuestionType() == "SS") //radio button
        {
            showRadioData(qObj)
        }
    }
    private fun showRadioData(qObj: QuestionData.QuestionAnsModel) {
        val recyclerLayoutManager = LinearLayoutManager(this)
        radioList.layoutManager = recyclerLayoutManager
        val str = qObj.getAnswerOptions()
        val result: List<String> = str!!.split("#")
        val radioRecyclerAdapter = RadioRecyclerViewAdapter(
            result,
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val ansObj = result[position]
                ansJsonObj!!.put("selected_answer", ansObj)
                if ( qObj.getAnswer() == "any") {

                    Log.e("RadioAns: $innerIndex", ": $ansObj")
                    if (innerIndex!! < (QArray!!.length()-1)) {
                        innerIndex = innerIndex!!.plus(1)
                        setDynamicData(innerIndex,ansJsonObj)
                    }else{
                        AppHelper.showToast(this,"GoTo Report generate")
                    }
                }
            })
        radioList.adapter = radioRecyclerAdapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.answerTxt -> {
                if (innerIndex!! < QArray!!.length() && innerIndex!! > 0) {
                    innerIndex = innerIndex!!.minus(1)
                    Log.e("back: ",": $innerIndex")
                    ansJsonObj = JSONObject()
                    setDynamicData(innerIndex, ansJsonObj)
                }
            }
        }
    }
}
