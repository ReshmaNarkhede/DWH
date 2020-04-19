package com.example.healthwareapplication.activity.question

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import org.json.JSONArray

class SubQuestionActivity : AppCompatActivity() {
    var index: Int? = 0

    private lateinit var subQuesArray: JSONArray
    private lateinit var subQText: TextView
    private lateinit var checkBoxList: RecyclerView
    private lateinit var radioList: RecyclerView
    private lateinit var seekBar: AppCompatSeekBar

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
        seekBar = findViewById(R.id.seekBar)
    }

    private fun defaultConfiguration() {
        val str = intent.getStringExtra("SubQArray")
        subQuesArray = JSONArray(str!!)
        setSubQData(index)
    }

    private fun setSubQData(index: Int?) {
        val subQObj = QuestionData.SubQuestionData(subQuesArray.getJSONObject(index!!))
        subQText.text = subQObj.getSubQuestion()
        if (subQObj.getSubQuestionType() == "CB") // CrossBar
        {
            seekBar.visibility = View.VISIBLE
            radioList.visibility = View.GONE
            checkBoxList.visibility = View.GONE

            showSeekData(subQObj)
        }
        if(subQObj.getSubQuestionType() == "SS") //radio button
        {
            seekBar.visibility = View.GONE
            radioList.visibility = View.VISIBLE
            checkBoxList.visibility = View.GONE

            showRadioData(subQObj)
        }
        if(subQObj.getSubQuestionType() == "MS") // checkbox
        {
            seekBar.visibility = View.GONE
            radioList.visibility = View.GONE
            checkBoxList.visibility = View.VISIBLE

            showCheckData(subQObj)

        }
    }
    private fun showSeekData(qObj: QuestionData.SubQuestionData) {

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
        index = index!!.plus(1)
        if(index!!>-1 && index!!<subQuesArray.length()){
            setSubQData(index)
        }

    }

    fun backClick(view: View) {
        index = index!!.minus(1)
        if(index!!>-1 && index!!<subQuesArray.length()){
            setSubQData(index)
        }
    }
}
