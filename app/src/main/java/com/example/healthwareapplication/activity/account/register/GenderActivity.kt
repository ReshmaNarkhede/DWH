package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_gender
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import kotlinx.android.synthetic.main.activity_gender.*

class GenderActivity : AppCompatActivity() {

    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_gender)

        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        userDetailModel = intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        if(userDetailModel.userType==1){
            userTypeTxt.text = getString(R.string.doctor)
        }else if(userDetailModel.userType==2){
            userTypeTxt.text = getString(R.string.human)
        }
    }

    fun maleClick(view: View) {
        userDetailModel.sex = "male"
        jumpNextActivity()
    }

    fun femaleClick(view: View) {
        userDetailModel.sex = "female"
        jumpNextActivity()
    }

    private fun jumpNextActivity() {
        val intent = Intent(this, AgeActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }

    fun humanClick(view: View) {
        finish()
    }
}
