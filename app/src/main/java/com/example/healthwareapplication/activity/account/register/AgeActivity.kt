package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_age
import com.example.healthwareapplication.activity.account.LetsMeetActivity
import com.example.healthwareapplication.activity.account.login.LoginActivity
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import kotlinx.android.synthetic.main.activity_age.*

class AgeActivity : AppCompatActivity() {
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_age)
        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        userDetailModel = intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        if(userDetailModel.sex == "male"){
            genderTxt.text = getString(R.string.male)
        }else{
            genderTxt.text = getString(R.string.female)
        }
    }

    fun genderClick(view: View) {
        finish()
    }

    fun yesClick(view: View) {
        val intent = Intent(this, DobActivity::class.java)
        startActivity(intent)
    }

    fun noClick(view: View) {
        val intent = Intent(this, LetsMeetActivity::class.java)
        intent.putExtra(IntentConstants.kLETS_MEET_MSG,true)
        startActivity(intent)
    }
}