package com.example.healthwareapplication.fragment.self_assessment.question

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.healthwareapplication.R
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class QFragment : Fragment(), View.OnClickListener {
    private var qObj: JSONObject? = JSONObject()
    private var index: Int? = 0
    private lateinit var questionView: LinearLayout
    private lateinit var nextBtn: Button
    private val Allcheck: MutableList<Boolean> = ArrayList()
    private var mOnButtonClickListener: OnButtonClickListener? = null

    private val AnswerSelected: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_q, container, false)

        initComponents(view!!)
        defaultConfiguration()
        return view
    }

    private fun initComponents(view: View) {

        questionView = view.findViewById(R.id.questionView)
        nextBtn = view.findViewById(R.id.nextBtn)
        nextBtn.setOnClickListener(this)

    }

    private fun defaultConfiguration() {
        val qAryStr = arguments!!.getString(AppConstants.kQUESTION_ARY)
        index = arguments!!.getInt(AppConstants.kINDEX)
        val qJsonAry = JSONArray(qAryStr)
        qObj = qJsonAry.getJSONObject(index!!)
        val questionData = QuestionData(qObj!!)
        val qView: View = getQuestionView(questionData)
        questionView.addView(qView)
    }

    companion object {
        fun newInstance(qAry: JSONArray, index: Int): QFragment {

            val onBoardFragment = QFragment()
            val bundle = Bundle(1)
            bundle.putString(AppConstants.kQUESTION_ARY, qAry.toString())
            bundle.putInt(AppConstants.kINDEX, index)
            onBoardFragment.arguments = bundle

            return onBoardFragment

        }
    }

    private fun getQuestionView(questionData: QuestionData): View {
        var view: View? = null
        if (questionData.getQuestionType() == "SS") // yes no option
        {
            view = layoutInflater.inflate(R.layout.survay_radio_button_view, null)
            val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
            val question = view.findViewById<TextView>(R.id.question)
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
                if (questionData.isSelected()) {
                    AnswerSelected.add(answerData.getAnswerId()!!)
                    radio.isChecked = true
                } else {
                    radio.isChecked = false
                }
//                if (questions.get(j).getShowOptions() != null) {
//                    radio.setOnCheckedChangeListener { compoundButton, b ->
//                        if (b) {
//                            ShowView(questions.get(j).getAnsId().toString() + "")
//                        } else {
//                            HideView(questions.get(j).getAnsId().toString() + "")
//                        }
//                    }
//                }
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
                val radio =
                    layoutInflater.inflate(R.layout.survay_radio_button, null) as RadioButton
                radio.id = j + 1001
                radio.text = answerData.getAnswerValue()
                radio.tag = answerData.getAnswerId()
                if (questionData.isSelected()) {
                    AnswerSelected.add(answerData.getAnswerId()!!)
                    radio.isChecked = true
                } else {
                    radio.isChecked = false
                }
//                if (questions.get(j).getShowOptions() != null) {
//                    radio.setOnCheckedChangeListener { compoundButton, b ->
//                        if (b) {
//                            ShowView(questions.get(j).getAnsId().toString() + "")
//                        } else {
//                            HideView(questions.get(j).getAnsId().toString() + "")
//                        }
//                    }
//                }
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.nextBtn -> {
                saveQuestionData()
                mOnButtonClickListener!!.onButtonClicked(v)
            }
        }

    }

    private fun saveQuestionData() {
        val listHashMap: HashMap<Int, QuestionData> = LinkedHashMap<Int, QuestionData>()
        val qData: QuestionData = setQuestionView(questionView, QuestionData(qObj!!))!!
//        for (i in 0 until qJsonAry.length()) {
//            val qObj = qJsonAry.getJSONObject(i)
//            val qData:QuestionData = SetQuestionView(questionView.getChildAt(i), QuestionData(qObj))!!
//            listHashMap[i] = qData
//        }
        listHashMap[index!!] = qData

        // Toast.makeText(this, "clicked acti", Toast.LENGTH_SHORT).show();
        val gson = Gson()
        val question = gson.toJson(listHashMap)
        Log.e("DATA: ", "$index, $question")
    }

    private fun setQuestionView(view: View?, questionData: QuestionData): QuestionData? {
        if (questionData.getQuestionType().equals("SS")) // yes no option
        {
            val radioGroup = view!!.findViewById<RadioGroup>(R.id.radioGroup)
            val question = view.findViewById<TextView>(R.id.question)
            val comment = view.findViewById<EditText>(R.id.comment)
            val answerArray = questionData.getAnswers()
            for (j in 0 until answerArray!!.length()) {
                val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
                val radio = radioGroup.getChildAt(j) as RadioButton
                if (radio.isChecked) {
                    if (answerData.getAnswerValue()!! == radio.text) {
                        Allcheck.add(true)
                    } else {
                        Allcheck.add(false)
                    }
                    questionData.setSelected(true)
                } else
                    questionData.setSelected(false)
            }
        }
        if (questionData.getQuestionType().equals("CB")) // yes no option
        {
            val radioGroup = view!!.findViewById<RadioGroup>(R.id.radioGroup)
            val question = view.findViewById<TextView>(R.id.question)
            val comment = view.findViewById<EditText>(R.id.comment)
            val answerArray = questionData.getAnswers()
            for (j in 0 until answerArray!!.length()) {
                val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
                val radio = radioGroup.getChildAt(j) as RadioButton
                if (radio.isChecked) {
                    if (answerData.getAnswerValue()!! == radio.text) {
                        Allcheck.add(true)
                    } else {
                        Allcheck.add(false)
                    }
                    questionData.setSelected(true)
                }else
                    questionData.setSelected(false)
            }
        }
        if (questionData.getQuestionType().equals("MS")) //Checkbox
        {
            val checkboxGroup =
                view!!.findViewById<LinearLayout>(R.id.checkboxGroup)
            val question = view.findViewById<TextView>(R.id.question)
            val comment = view.findViewById<EditText>(R.id.comment)
            val answerArray = questionData.getAnswers()
            for (j in 0 until answerArray!!.length()) {
                val radio = checkboxGroup.getChildAt(j) as CheckBox
                if (radio.isChecked) {
                    questionData.setSelected(true)
                } else questionData.setSelected(false)
            }
        }

        return questionData
    }

    internal interface OnButtonClickListener {
        fun onButtonClicked(view: View?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnButtonClickListener = try {
            context as OnButtonClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context as Activity).localClassName + " must implement OnButtonClickListener"
            )
        }
    }
}
