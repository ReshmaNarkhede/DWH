package com.example.healthwareapplication.activity.account.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_terms_and_policy
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import kotlinx.android.synthetic.main.activity_terms_and_policy.*


class TermsAndPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_terms_and_policy)

        initComponents()
        defaultConfig()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfig() {
        termsTxt.makeLinks(
            Pair(getString(R.string.terms_span), View.OnClickListener {
                val intent = Intent(this, TermsDataActivity::class.java)
                intent.putExtra(IntentConstants.kLABEL, getString(R.string.terms_span))
                startActivity(intent)
            })
        )
        policyTxt.makeLinks(
            Pair(getString(R.string.policy_span), View.OnClickListener {
                val intent = Intent(this, TermsDataActivity::class.java)
                intent.putExtra(IntentConstants.kLABEL, getString(R.string.policy_span))
                startActivity(intent)
            })
        )
    }

    fun parentClick(view: View) {
        checkValidation()
    }

    private fun checkValidation() {
        if (chk1.isChecked && chk2.isChecked && chk3.isChecked) {
            intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            AppHelper.showToast(this, "Please check terms and policy to register.")
        }
    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}