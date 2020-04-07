package com.example.healthwareapplication.fragment.self_assessment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

import com.example.healthwareapplication.R
import com.richpath.RichPath
import com.richpath.RichPathView

class BodyFragment : Fragment() {
    private lateinit var bodyImg: RichPathView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val view: View? = inflater.inflate(R.layout.fragment_body, container, false)
        initComponents(view)
        defaultConfiguration()
        return view
    }

    private fun initComponents(view: View?) {
        bodyImg = view!!.findViewById(R.id.bodyImg)
    }

    private fun defaultConfiguration() {
        val array = bodyImg.findAllRichPaths()
        bodyImg.setOnPathClickListener(RichPath.OnPathClickListener { richPath ->

            for (i in array.indices) {
                val obj: RichPath = bodyImg.findRichPathByIndex(i)!!

                if (richPath.name == obj.name) {
                    obj.fillColor = ContextCompat.getColor(activity!!, R.color.body_fill)
                } else {
                    obj.fillColor = ContextCompat.getColor(activity!!, R.color.white)
                    obj.strokeColor = ContextCompat.getColor(activity!!, R.color.body_outline)
                }
            }
        })
    }
}
