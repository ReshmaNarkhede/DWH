package com.example.healthwareapplication.fragment.self_assessment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.healthwareapplication.R
import com.example.healthwareapplication.model.self_assessment.QuestionData
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import org.json.JSONArray

class QuestionFragment : Fragment(),BlockingStep {
    private var position: Int? = null
    private var questionAry: JSONArray? = null
    private lateinit var questionView: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view:View? =  inflater.inflate(R.layout.fragment_question, container, false)
        initComponents(view)
        return view
    }

    private fun initComponents(view: View?) {
        questionView = view!!.findViewById(R.id.questionView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("Position")
            val questionAryStr = it.getString("Array")
            questionAry = JSONArray(questionAryStr)
        }
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback!!.goToPrevStep()
    }

    override fun onSelected() {
        val questionData = QuestionData(questionAry!!.getJSONObject(position!!))
        val view: View = getQuestionView(questionData)
        questionView.addView(view)
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


    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {

        callback!!.complete()
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        Log.e("onnext: ","click")
        callback!!.goToNextStep()
    }

    override fun verifyStep(): VerificationError? {
     return null
    }

    override fun onError(error: VerificationError) {
    }
}
