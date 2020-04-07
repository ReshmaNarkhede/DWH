package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.country.CountryActivity
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel

class GenderActivity : AppCompatActivity() {

    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)

        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        userDetailModel = intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
    }

    fun maleClick(view: View) {
        userDetailModel!!.sex = "male"
        jumpNextActivity()
    }

    fun femaleClick(view: View) {
        userDetailModel!!.sex = "female"
        jumpNextActivity()
    }

    fun transClick(view: View) {
        userDetailModel!!.sex = "eunuch"
        jumpNextActivity()
    }

    private fun jumpNextActivity() {
        val intent = Intent(this, CountryActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }
}
