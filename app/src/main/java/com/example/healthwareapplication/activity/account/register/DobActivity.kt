package com.example.healthwareapplication.activity.account.register

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_dob

import com.example.healthwareapplication.app_utils.AppHelper

import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel

import kotlinx.android.synthetic.main.activity_dob.*
import java.text.SimpleDateFormat

class DobActivity : AppCompatActivity() {
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_dob)

        initComponents()
    }

    private fun initComponents() {
        userDetailModel = intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
    }

    fun ageTxtClick(view: View) {
        finish()
    }

    fun dobDateClick(view: View) {
        val listner = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val monthOfYear = month + 1
            val mt: String
            val dy: String //local variable

            mt = if (monthOfYear < 10) "0$monthOfYear" //if month less than 10 then ad 0 before month
            else java.lang.String.valueOf(monthOfYear)

            dy = if (dayOfMonth < 10) "0$dayOfMonth" else java.lang.String.valueOf(dayOfMonth)

            dobDate.text = "$year-$mt-$dy"
            DialogUtility.hideProgressDialog()
            userDetailModel.dob = dobDate.text.toString()
            Log.e("when Date: ", " : ${dobDate.text}")
                openNextActivity()
        }
        DialogUtility.showDOBDatePickerDialog(this, listner).show()
    }

    fun dobTimeClick(view: View) {
        val listner =
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                dobTime.text =
                    "${SimpleDateFormat("hh:mm a").format(SimpleDateFormat("hh:mm").parse("${selectedHour}:${selectedMinute}"))}"
                DialogUtility.hideProgressDialog()
                Log.e("when Time: ", " : ${dobTime.text}")
                userDetailModel.tob = dobTime.text.toString()
                if (dobDate.text.toString() == resources.getString(R.string.date)) {
                    AppHelper.showToast(this, getString(R.string.valid_date_msg))
                } else {
                    openNextActivity()
                }
            }
        DialogUtility.showTimePickerDialog(this, listner).show()
    }
    private fun openNextActivity() {

        val intent = Intent(this, CountryActivity::class.java)
        intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
        startActivity(intent)
    }

}