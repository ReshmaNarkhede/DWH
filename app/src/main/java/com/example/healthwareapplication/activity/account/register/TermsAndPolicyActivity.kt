package com.example.healthwareapplication.activity.account.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityTermsAndPolicyBinding


class TermsAndPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsAndPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfig()
        initListener()
    }

    private fun initListener() {
        binding.backBtn.setOnClickListener {
            checkValidation()
        }
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
    }

    private fun defaultConfig() {
        binding.termsTxt.makeLinks(
            Pair(getString(R.string.terms_span), View.OnClickListener {
                val intent = Intent(this, TermsDataActivity::class.java)
                intent.putExtra(IntentConstants.kLABEL, getString(R.string.terms_span))
                startActivity(intent)
            })
        )
        binding.policyTxt.makeLinks(
            Pair(getString(R.string.policy_span), View.OnClickListener {
                val intent = Intent(this, TermsDataActivity::class.java)
                intent.putExtra(IntentConstants.kLABEL, getString(R.string.policy_span))
                startActivity(intent)
            })
        )
    }

    private fun checkValidation() {
        if (binding.chk1.isChecked && binding.chk2.isChecked && binding.chk3.isChecked) {
            intent = Intent()
            binding.errorText.visibility = View.INVISIBLE
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            binding.errorText.visibility = View.VISIBLE
            binding.errorText.text = getString(R.string.terms_error)
//            AppHelper.showToast(this, "Please check terms and policy to register.")
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