package com.example.healthwareapplication.activity.account.register

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityDobBinding
import com.example.healthwareapplication.model.user.UserDetailModel
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import java.text.SimpleDateFormat
import java.util.*


class DobActivity : AppCompatActivity(),
    com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityDobBinding
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDobBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initComponents()
        defaultConfig()
    }

    private fun defaultConfig() {
        binding.dobDate.setOnClickListener(View.OnClickListener {
            showDate(R.style.NumberPickerStyle)
//            dobDateClick()
        })
    }

    private fun initComponents() {
        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        userDetailModel =
            intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
    }

    fun ageTxtClick(view: View) {
        finish()
    }

    //    fun dobDateClick() {
//        val listner = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//            val monthOfYear = month + 1
//            val mt: String
//            val dy: String //local variable
//
//            mt =
//                if (monthOfYear < 10) "0$monthOfYear" //if month less than 10 then ad 0 before month
//                else java.lang.String.valueOf(monthOfYear)
//
//            dy = if (dayOfMonth < 10) "0$dayOfMonth" else java.lang.String.valueOf(dayOfMonth)
//
//            dobDate.text = "$year-$mt-$dy"
//            DialogUtility.hideProgressDialog()
//            userDetailModel.dob = dobDate.text.toString()
//            Log.e("when Date: ", " : ${dobDate.text}")
//            openNextActivity()
//        }
//        DialogUtility.showDOBDatePickerDialog(this, listner).show()
//    }
    fun showDate(spinnerTheme: Int) {
        val calendar = Calendar.getInstance()
        val cYear = calendar[Calendar.YEAR]
        val cMonth = calendar[Calendar.MONTH]
        val cDay = calendar[Calendar.DAY_OF_MONTH]
        val year = cYear - 16
        Log.e("year: ", "" + year)
        SpinnerDatePickerDialogBuilder()
            .context(this)
            .callback(this)
            .maxDate(year, cMonth, cDay)
            .spinnerTheme(spinnerTheme)
            .defaultDate(cYear, cMonth, cDay)
            .build()
            .show()

    }

    fun dobTimeClick(view: View) {
        val listner =
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                binding.dobTime.text =
                    "${SimpleDateFormat("hh:mm a").format(SimpleDateFormat("hh:mm").parse("${selectedHour}:${selectedMinute}"))}"
                DialogUtility.hideProgressDialog()
                userDetailModel.tob = binding.dobTime.text.toString()
                if (binding.dobDate.text.toString() == resources.getString(R.string.date)) {
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


    override fun onDateSet(
        view: com.tsongkha.spinnerdatepicker.DatePicker?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
        userDetailModel.dob = simpleDateFormat.format(calendar.time)
        binding.dobDate.text = AppHelper.getDobFormat(simpleDateFormat.format(calendar.time))
        openNextActivity()
    }

}