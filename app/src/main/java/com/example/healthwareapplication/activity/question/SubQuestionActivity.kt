package com.example.healthwareapplication.activity.question

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.adapter.self_assessment.question.CheckRecyclerViewAdapter
import com.example.healthwareapplication.adapter.self_assessment.question.RadioRecyclerViewAdapter
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.RecyclerItemClickListener
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.warkiz.widget.*
import org.json.JSONArray
import org.json.JSONObject

class SubQuestionActivity : AppCompatActivity() {
    var index: Int? = 0

    private lateinit var subQuesArray: JSONArray
    private lateinit var subQText: TextView
    private lateinit var checkBoxList: RecyclerView
    private lateinit var radioList: RecyclerView
    private lateinit var seekBar: AppCompatSeekBar
    private lateinit var seekbarParent: LinearLayout
    private lateinit var answerTxt: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_question)

        initComponents()
        defaultConfiguration()
    }
    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        subQText = findViewById(R.id.subQuestionTxt)
        checkBoxList = findViewById(R.id.checkBoxList)
        radioList = findViewById(R.id.radioList)
        seekbarParent = findViewById(R.id.seekbarParent)
        answerTxt = findViewById(R.id.answerTxt)

    }

    private fun defaultConfiguration() {
        val str = intent.getStringExtra("SubQArray")
        val ansStr = intent.getStringExtra("AnsObj")
        subQuesArray = JSONArray(str!!)
        val ansObj = QuestionData.AnswerData(JSONObject(ansStr))
        setSubQData(index)
    }

    private fun setSubQData(index: Int?) {
        val subQObj = QuestionData.SubQuestionData(subQuesArray.getJSONObject(index!!))
//        answerTxt.text = ansObj.getAnswerValue()
        subQText.text = subQObj.getSubQuestion()
        if (subQObj.getSubQuestionType() == "CB") // CrossBar
        {
            seekbarParent.visibility = View.VISIBLE
            radioList.visibility = View.GONE
            checkBoxList.visibility = View.GONE

            showSeekData(subQObj)
        }
        if(subQObj.getSubQuestionType() == "SS") //radio button
        {
            seekbarParent.visibility = View.GONE
            radioList.visibility = View.VISIBLE
            checkBoxList.visibility = View.GONE

            showRadioData(subQObj)
        }
        if(subQObj.getSubQuestionType() == "MS") // checkbox
        {
            seekbarParent.visibility = View.GONE
            radioList.visibility = View.GONE
            checkBoxList.visibility = View.VISIBLE

            showCheckData(subQObj)

        }
    }
    private fun showSeekData(qObj: QuestionData.SubQuestionData) {
        seekbarParent.removeAllViews()
        val arr = toStringArray(qObj.getSubQuestionAnswers())
        val seekBar: IndicatorSeekBar = IndicatorSeekBar
            .with(this)
            .progress(0f)
            .tickCount(qObj.getSubQuestionAnswers()!!.length() + 1)
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
//                val ansObj = getAnsObj(seekParams.tickText, qObj)
//                fnlAnsArray.put(ansObj)
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
        }
    }

    private fun showRadioData(
        qObj: QuestionData.SubQuestionData
    ) {
        val recyclerLayoutManager = LinearLayoutManager(this)
        radioList.layoutManager = recyclerLayoutManager

        val radioRecyclerAdapter = RadioRecyclerViewAdapter(qObj.getSubQuestionAnswers(), RecyclerItemClickListener.OnItemClickListener { view, position ->
            val obj = QuestionData.AnswerData(qObj.getSubQuestionAnswers()!!.getJSONObject(position))
            Log.e("sub radio act: "," " + obj.getAnswerValue())
        })

        radioList.adapter = radioRecyclerAdapter
    }

    private fun showCheckData(qObj: QuestionData.SubQuestionData) {
        val recyclerLayoutManager = LinearLayoutManager(this)
        checkBoxList.layoutManager = recyclerLayoutManager

        val chkRecyclerAdapter = CheckRecyclerViewAdapter(qObj.getSubQuestionAnswers(), RecyclerItemClickListener.OnItemClickListener { view, position ->
            val chkObj = QuestionData.AnswerData(qObj.getSubQuestionAnswers()!!.getJSONObject(position))
            AppHelper.showToast(this,chkObj.getAnswerValue()+"")
            Log.e("sub check act: "," " + chkObj.getAnswerValue())
        })
        checkBoxList.adapter = chkRecyclerAdapter
    }

    fun nextClick(view: View) {
        if (index!! < (subQuesArray.length() - 1)) {
            index = index!!.plus(1)
            setSubQData(index)
        }
    }

    fun backClick(view: View) {
        if (index!! < subQuesArray.length() && index!! > 0) {
            index = index!!.minus(1)
            setSubQData(index)
        }
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
}
