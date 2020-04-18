package com.example.healthwareapplication.fragment.self_assessment.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.healthwareapplication.R
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import org.json.JSONArray

class SubQuestionFragment : Fragment(), BlockingStep {
    private var position: Int? = null
    private var subQArray: JSONArray? = null
    private lateinit var subQView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("SubQPosition")
            val subAry = it.getString("SubQArray")
            subQArray = JSONArray(subAry)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_sub_question, container, false)
        initComponents(view)
        return view
    }

    private fun initComponents(view: View?) {
        subQView = view!!.findViewById(R.id.subQView)
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback!!.goToPrevStep()
    }

    override fun onSelected() {
        subQView.removeAllViews()
        val questionData = QuestionData.SubQuestionData(subQArray!!.getJSONObject(position!!))
        val view: View = getQuestionView(questionData)
        subQView.addView(view)
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        callback!!.complete()
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        callback!!.goToNextStep()
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {
    }

    private fun getQuestionView(questionData: QuestionData.SubQuestionData): View {
        var view: View? = null
        if (questionData.getSubQuestionType() == "SS") // yes no option
        {
            view = loadSSData(questionData)
        }
        if (questionData.getSubQuestionType() == "CB") // CrossBar
        {
            view = loadCBData(questionData)
        }
        if (questionData.getSubQuestionType() == "MS") // Checkbox
        {
            view = loadMSData(questionData)
        }
        return view!!
    }

    private fun loadSSData(questionData: QuestionData.SubQuestionData): View {
        val view = layoutInflater.inflate(R.layout.survay_radio_button_view, null)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val questionTxt = view.findViewById<TextView>(R.id.question)
        radioGroup.removeAllViews()
        questionTxt.text = questionData.getSubQuestion()
        val answerArray = questionData.getSubQuestionAnswers()
        for (j in 0 until answerArray!!.length()) {
            val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
            val radio =
                layoutInflater.inflate(R.layout.survay_radio_button, null) as RadioButton
            radio.id = j + 1001
            radio.text = answerData.getAnswerValue()
            radio.tag = answerData.getAnswerId()
            radioGroup.addView(radio)
        }

        return view
    }

    private fun loadCBData(questionData: QuestionData.SubQuestionData): View? {
        val view = layoutInflater.inflate(R.layout.survay_radio_button_view, null)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val questionTxt = view.findViewById<TextView>(R.id.question)

        radioGroup.removeAllViews()
        questionTxt.text = questionData.getSubQuestion()
        val answerArray = questionData.getSubQuestionAnswers()
        for (j in 0 until answerArray!!.length()) {
            val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
            val radio =
                layoutInflater.inflate(R.layout.survay_radio_button, null) as RadioButton
            radio.id = j + 1001
            radio.text = answerData.getAnswerValue()
            radio.tag = answerData.getAnswerId()
            radioGroup.addView(radio)
        }

        return view
    }

    private fun loadMSData(questionData: QuestionData.SubQuestionData): View? {
        val view = layoutInflater.inflate(R.layout.survay_check_box_view, null)
        val checkboxGroup = view.findViewById<LinearLayout>(R.id.checkboxGroup)
        val questionTxt = view.findViewById<TextView>(R.id.question)
        questionTxt.text = questionData.getSubQuestion()
        val answerArray = questionData.getSubQuestionAnswers()
        for (j in 0 until answerArray!!.length()) {
            val answerData = QuestionData.AnswerData(answerArray.getJSONObject(j))
            val checkbox = layoutInflater.inflate(R.layout.survay_check_box, null) as CheckBox
            checkbox.id = j + 1001
            checkbox.text = answerData.getAnswerValue()
            checkbox.tag = answerData.getAnswerId()
            checkboxGroup.addView(checkbox)
        }

        return view
    }

}
