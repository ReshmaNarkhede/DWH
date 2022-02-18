package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.activity.account.LetsMeetActivity
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityAgeBinding
import com.example.healthwareapplication.model.user.UserDetailModel

class AgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgeBinding
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        userDetailModel = intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        if(userDetailModel.userType == 1){
            binding.userTypeTxt.text = getString(R.string.doctor)
        }else{
            binding.userTypeTxt.text = getString(R.string.human)
        }
    }

    fun genderClick(view: View) {
        finish()
    }

    fun yesClick(view: View) {
        val intent = Intent(this, RegisterInfoActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }

    fun noClick(view: View) {
        val intent = Intent(this, LetsMeetActivity::class.java)
        intent.putExtra(IntentConstants.kLETS_MEET_MSG,true)
        startActivity(intent)
    }
}