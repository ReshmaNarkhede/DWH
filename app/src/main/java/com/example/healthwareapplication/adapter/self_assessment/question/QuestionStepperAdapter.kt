package com.example.healthwareapplication.adapter.self_assessment.question

import android.content.Context
import android.os.Bundle
import androidx.annotation.IntRange
import androidx.fragment.app.FragmentManager
import com.example.healthwareapplication.fragment.self_assessment.question.QuestionFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.json.JSONArray

class QuestionStepperAdapter(fm: FragmentManager, context: Context, private var questionAry: JSONArray) : AbstractFragmentStepAdapter(fm, context) {
    override fun getCount(): Int {
        return questionAry.length()
    }

    override fun createStep(position: Int): Step {
        val step1 =
            QuestionFragment()
        val b1 = Bundle()
        b1.putInt("Position", position)
        b1.putString("Array", questionAry.toString())
        step1.arguments = b1
        return step1
    }

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        return StepViewModel.Builder(context)
            .setTitle("Tabs 1") //can be a CharSequence instead
            .create()
    }
}