package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_register_name.*

class RegisterNameActivity : AppCompatActivity() {
    private lateinit var userDetailModel: UserDetailModel
    private lateinit var name: TextInputEditText
    private lateinit var surname: TextInputEditText
    private lateinit var email: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_name)

        initComponents()
        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        if (userDetailModel?.userType == 3) {
            dr.visibility = View.VISIBLE
        } else {
            dr.visibility = View.GONE
        }
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        name = findViewById(R.id.name)
        surname = findViewById(R.id.surname)
        email = findViewById(R.id.email)
    }

    fun okehClick(view: View) {
        if (name.text.toString().equals("")) {
            name.error = resources.getString(R.string.errormessage)
        } else if (surname.text.toString().equals("")) {
            surname.error = resources.getString(R.string.errormessage)
        } else if (email.text.toString().trim().equals("") || !AppHelper.isValidEmail(email.text.toString().trim())) {
            email.error = resources.getString(R.string.errormessage)
        } else {
            userDetailModel?.firstName = name.text.toString()
            userDetailModel?.lastName = surname.text.toString()
            userDetailModel?.email = email.text.toString()

            if (userDetailModel.userType == 3) {
//                DoctorSpecialityActivity.start(this, userDetailModel!!)
            } else {
                val intent = Intent(this, RegisterPasswordActivity::class.java)
                intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
                startActivity(intent)
            }
        }
    }
}
