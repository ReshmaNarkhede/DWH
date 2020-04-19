package com.example.healthwareapplication.activity.question

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
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


class QuestionDemoActivity : AppCompatActivity() {
    private lateinit var questionAry: JSONArray
    var index: Int? = 0
    private lateinit var questionTxt: TextView
    private lateinit var checkBoxList: RecyclerView
    private lateinit var radioList: RecyclerView
    private lateinit var seekbarParent: LinearLayout
    private lateinit var backBtn: Button
    private lateinit var nextBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_demo)
        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        questionTxt = findViewById(R.id.questionTxt)
        checkBoxList = findViewById(R.id.checkBoxList)
        radioList = findViewById(R.id.radioList)
        seekbarParent = findViewById(R.id.seekbarParent)
        backBtn = findViewById(R.id.backBtn)
        nextBtn = findViewById(R.id.nextBtn)
    }

    private fun defaultConfiguration() {

        questionAry = AppSessions.getQuestionData(this)!!
        if (questionAry.length() == 0) {
            fetchQuestionData("4")
        } else {
            setDynamicData(index)
        }
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
                        questionAry = responseModel.getDataArray()!!
                        AppSettings.setJsonArrayValue(
                            this@QuestionDemoActivity,
                            AppConstants.kQUESTION_ARY,
                            questionAry.toString()
                        )
                        Log.e("QArySize: ", " " + questionAry.length())
                        setDynamicData(index)
                    } else {
                        AppHelper.showToast(
                            this@QuestionDemoActivity,
                            responseModel.getMessage().toString()
                        )
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

    private fun setDynamicData(index: Int?) {
        Log.e("Index: ", " $index")
        val qObj = QuestionData(questionAry.getJSONObject(index!!))
        questionTxt.text = qObj.getQuestion()
        if (qObj.getQuestionType() == "CB") // CrossBar
        {
            seekbarParent.visibility = View.VISIBLE
//            seekBar.visibility = View.VISIBLE
            radioList.visibility = View.GONE
            checkBoxList.visibility = View.GONE

            showSeekData(qObj)
        }
        if (qObj.getQuestionType() == "SS") //radio button
        {
            seekbarParent.visibility = View.GONE
            radioList.visibility = View.VISIBLE
            checkBoxList.visibility = View.GONE

            showRadioData(qObj)
        }
        if (qObj.getQuestionType() == "MS") // checkbox
        {
            seekbarParent.visibility = View.GONE
            radioList.visibility = View.GONE
            checkBoxList.visibility = View.VISIBLE

            showCheckData(qObj)

        }
    }

    private fun showSeekData(qObj: QuestionData) {
        seekbarParent.removeAllViews()
        val arr = toStringArray(qObj.getAnswers())
        Log.e("ary: ", "arr: " + arr!!.size)
        val seekBar: IndicatorSeekBar = IndicatorSeekBar
            .with(this)
            .progress(0f)
            .tickCount(qObj.getAnswers()!!.length())
            .showTickMarksType(TickMarkType.OVAL)
            .tickTextsArray(arr)
            .showTickTexts(true)
            .tickTextsColorStateList(
                resources.getColorStateList(R.color.colorPrimary)
            )
            .indicatorColor(resources.getColor(R.color.colorAccent))
            .indicatorTextColor(Color.parseColor("#ffffff"))
            .showIndicatorType(IndicatorType.ROUNDED_RECTANGLE)
            .thumbColor(Color.parseColor("#ff0000"))
            .thumbSize(14)
            .trackProgressColor(resources.getColor(R.color.colorAccent))
            .trackProgressSize(4)
            .trackBackgroundColor(resources.getColor(R.color.grey))
            .trackBackgroundSize(2)
            .build()
        seekbarParent.addView(seekBar)
        seekBar.setOnSeekChangeListener(object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                Log.e("selected seek", seekParams.tickText)
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        })
    }

    fun toStringArray(array: JSONArray?): Array<String?>? {
        if (array == null) return null
        val arr = arrayOfNulls<String>(array.length())
        for (i in arr.indices) {
//            arr[i] = array.optString(i)
            val obj = QuestionData.AnswerData(array.optJSONObject(i)).getAnswerValue()
            arr[i] = obj
        }
        return arr
    }

    private fun showRadioData(
        qObj: QuestionData
    ) {
        val recyclerLayoutManager = LinearLayoutManager(this)
        radioList.layoutManager = recyclerLayoutManager

        val radioRecyclerAdapter = RadioRecyclerViewAdapter(
            qObj.getAnswers(),
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val obj = QuestionData.AnswerData(qObj.getAnswers()!!.getJSONObject(position))
                Log.e("radio act: ", " " + obj.getAnswerValue())
                if (obj.isSubQuestion() == "1") {
                    val intent = Intent(this, SubQuestionActivity::class.java)
                    intent.putExtra("SubQArray", qObj.getSubQuestions().toString())
                    startActivity(intent)
                }
            })

        radioList.adapter = radioRecyclerAdapter
    }

    private fun showCheckData(qObj: QuestionData) {
        val recyclerLayoutManager = LinearLayoutManager(this)
        checkBoxList.layoutManager = recyclerLayoutManager

        val chkRecyclerAdapter = CheckRecyclerViewAdapter(
            qObj.getAnswers(),
            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val chkObj = QuestionData.AnswerData(qObj.getAnswers()!!.getJSONObject(position))
                AppHelper.showToast(this, chkObj.getAnswerValue() + "")
                Log.e("check act: " + qObj.getQuestion(), " " + chkObj.getAnswerValue())
            })
        checkBoxList.adapter = chkRecyclerAdapter
    }

    fun nextClick(view: View) {
        if (index!! < (questionAry.length() - 1)) {
                index = index!!.plus(1)
                setDynamicData(index)
        }
    }

    fun backClick(view: View) {
        if (index!! < questionAry.length() && index!! > 0) {
//            if (index == 0) {
//                backBtn.visibility = View.GONE
//            } else {
                index = index!!.minus(1)
                setDynamicData(index)
//                backBtn.visibility = View.VISIBLE
//            }
        }
    }

}
