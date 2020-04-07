package com.example.healthwareapplication.activity.account.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.register.RegisterIntroActivity
import com.example.healthwareapplication.activity.country.CountryActivity
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.google.android.material.textfield.TextInputEditText

class HaveWeMetActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_have_we_met)
    }

    fun noClick(v: View) {
        val intent = Intent(this, RegisterIntroActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, this.intent?.getSerializableExtra(IntentConstants.kUSER_DATA))
        startActivity(intent)
    }

    fun yeahClick(v: View) {
        val intent = Intent(this, CountryActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_TYPE, 1)
        intent.putExtra(IntentConstants.kUSER_DATA, this.intent?.getSerializableExtra(IntentConstants.kUSER_DATA))
        startActivity(intent)
    }

    override fun onClick(v: View?) {

    }
}
