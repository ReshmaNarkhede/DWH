package com.example.healthwareapplication.activity.question

import android.content.ClipData.Item
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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


class QuestionDemoActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var chkRecyclerAdapter: CheckRecyclerViewAdapter
    var fnlAnsArray: JSONArray = JSONArray()
//    var ansObj: JSONObject = JSONObject()
    private lateinit var questionAry: JSONArray
    var index: Int? = 0
    private lateinit var questionTxt: TextView
    private lateinit var answerTxt: TextView
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
        answerTxt = findViewById(R.id.answerTxt)
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
            setDynamicData(index,  fnlAnsArray)
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
                        setDynamicData(index, fnlAnsArray)
                    } else {
                        AppHelper.showToast(this@QuestionDemoActivity, responseModel.getMessage().toString())
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

    private fun setDynamicData(index: Int?, fnlAnsArray: JSONArray) {
        val ansList:MutableList<String> = ArrayList<String>()
        for (i in 0 until fnlAnsArray.length()){
            val obj = QuestionData.AnswerData(fnlAnsArray.getJSONObject(i))
            ansList.add(obj.getAnswerValue()!!)
        }
        Log.e("ansList", " " + TextUtils.join( "--",ansList))
        val qObj = QuestionData(questionAry.getJSONObject(index!!))
        questionTxt.text = qObj.getQuestion()
        answerTxt.text = TextUtils.join( ",",ansList)
        answerTxt.setOnClickListener(this)
        if (qObj.getQuestionType() == "CB") // CrossBar
        {
            seekbarParent.visibility = View.VISIBLE
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
        val seekBar: IndicatorSeekBar = IndicatorSeekBar
            .with(this)
            .progress(0f)
            .tickCount(qObj.getAnswers()!!.length() + 1)
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
        seekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                val ansObj = getAnsObj(seekParams.tickText, qObj)
                fnlAnsArray.put(ansObj)
                if (QuestionData.AnswerData(ansObj).isSubQuestion() == "1") {
                    val intent = Intent(this@QuestionDemoActivity, SubQuestionActivity::class.java)
                    intent.putExtra("SubQArray", qObj.getSubQuestions().toString())
                    intent.putExtra("AnsObj", ansObj.toString())
                    startActivity(intent)
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }
    }

    private fun getAnsObj(tickText: String?, qObj: QuestionData): JSONObject {
        var ansObj: JSONObject = JSONObject()
        for (i in 0 until qObj.getAnswers()!!.length()) {
            ansObj = qObj.getAnswers()!!.getJSONObject(i)
            val ansData = QuestionData.AnswerData(ansObj)
            if (ansData.getAnswerValue() == tickText) {
                return ansObj
            }
        }
        return ansObj
    }

    fun toStringArray(array: JSONArray?): Array<String?>? {
        if (array == null) return null
        val arr = arrayOfNulls<String>(array.length() + 1)
        arr[0] = "0"
        for (i in 1 until arr.size) {
            val obj = QuestionData.AnswerData(array.optJSONObject(i - 1)).getAnswerValue()
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
                val ansObj = qObj.getAnswers()!!.getJSONObject(position)
                fnlAnsArray.put(ansObj)
                val radioObj = QuestionData.AnswerData(qObj.getAnswers()!!.getJSONObject(position))
//                Log.e("radio act: ", " " + radioObj.getAnswerValue())
//                if (radioObj.isSubQuestion() == "1") {
//                    val intent = Intent(this, SubQuestionActivity::class.java)
//                    intent.putExtra("SubQArray", qObj.getSubQuestions().toString())
//                    startActivity(intent)
//                }
            })

        radioList.adapter = radioRecyclerAdapter
    }

    private fun showCheckData(qObj: QuestionData) {

        val recyclerLayoutManager = LinearLayoutManager(this)
        checkBoxList.layoutManager = recyclerLayoutManager

        chkRecyclerAdapter = CheckRecyclerViewAdapter(
            qObj.getAnswers(),

            RecyclerItemClickListener.OnItemClickListener { view, position ->
                val selected = chkRecyclerAdapter.getSelected()!!
                fnlAnsArray = selected
//                if (chkObj.isSubQuestion() == "1") {
//                    val intent = Intent(this, SubQuestionActivity::class.java)
//                    intent.putExtra("SubQArray", qObj.getSubQuestions().toString())
//                    startActivity(intent)
//                }
            })

        checkBoxList.adapter = chkRecyclerAdapter

    }

    fun nextClick(view: View) {
        Log.e("next Index", " $index")
        if (fnlAnsArray.length() > 0) {
            if (index!! < (questionAry.length() - 1)) {
                index = index!!.plus(1)
                setDynamicData(index, fnlAnsArray)
            }
            fnlAnsArray = JSONArray()
        } else {
            AppHelper.showToast(this, "Please select answer first")
        }

    }

    fun backClick(view: View) {
        if (index!! < questionAry.length() && index!! > 0) {
            index = index!!.minus(1)
            setDynamicData(index, fnlAnsArray)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.answerTxt -> {
                Log.e("ONCLICK: ",":${fnlAnsArray.length()}")
                if (index!! < questionAry.length() && index!! > 0) {
                    index = index!!.minus(1)
                    setDynamicData(index, fnlAnsArray)
                }
            }
        }

    }
}
