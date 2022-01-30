package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityGenderBinding
import com.example.healthwareapplication.model.user.UserDetailModel

class GenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenderBinding
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        userDetailModel = intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        if(userDetailModel.userType==1){
            binding.userTypeTxt.text = getString(R.string.doctor)
        }else if(userDetailModel.userType==2){
            binding.userTypeTxt.text = getString(R.string.human)
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
