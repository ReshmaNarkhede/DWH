package com.example.healthwareapplication.activity.account.register

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import java.text.SimpleDateFormat

class RegisterAgeActivity : AppCompatActivity() {
    private lateinit var time: TextView
    private lateinit var date: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_age)
        initComponents()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        time = findViewById(R.id.time)
        date = findViewById(R.id.date)
    }

    fun dateClick(view: View) {
        val listner = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            date.text = "" + dayOfMonth + "/" + monthOfYear + "/" + year
            DialogUtility.hideProgressDialog()
        }
        DialogUtility.showDatePickerDialog(this, listner).show()
    }

    fun timeClick(view: View) {
        val listner = TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
//            var status = "AM"
//
//            if (selectedHour > 11) {
//                status = "PM"
//            }
//            val hour_of_12_hour_format: Int
//
//            if (selectedHour > 11) {
//                hour_of_12_hour_format = selectedHour - 12
//            } else {
//                hour_of_12_hour_format = selectedHour
//            }
           time.text = "${SimpleDateFormat("hh:mm a").format(SimpleDateFormat("hh:mm").parse("${selectedHour}:${selectedMinute}"))}"
//            registration2Activity.time.text = "$hour_of_12_hour_format:$selectedMinute $status"
            DialogUtility.hideProgressDialog()
        }
        DialogUtility.showTimePickerDialog(this, listner).show()
    }

    fun okeyClick(view: View) {
        if (time.text.toString().equals("")) {
            Toast.makeText(this, R.string.errormessage, Toast.LENGTH_LONG).show()
        } else if (date.text.toString().equals("")) {
            Toast.makeText(this, R.string.errormessage, Toast.LENGTH_LONG).show()
        } else {
            val userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
            userDetailModel.dob = date.text.toString()
            userDetailModel.tob =time.text.toString()
            val intent = Intent(this, RegisterInfoActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        }
    }
}
