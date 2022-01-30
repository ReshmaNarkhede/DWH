package com.example.healthwareapplication.activity.self_assessment

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityWhenStartBinding
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class WhenStartActivity : AppCompatActivity(),
    com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityWhenStartBinding
    private var symptomStr: String? = null
    val delimiter = ","
    val finalStr = SpannableStringBuilder()
    private lateinit var simpleDateFormat: SimpleDateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhenStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        symptomStr = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
        val obj1 = JSONObject(symptomStr!!)
        val symptmJsonAry = JSONArray()
        symptmJsonAry.put(obj1)
        for (i in 0 until symptmJsonAry.length()) {
            val obj = SymptomJsonModel(symptmJsonAry.getJSONObject(i))
            finalStr.append(delimiter)
            finalStr.append(obj.getName())
        }
        Log.e("STr: ", ": " + finalStr.toString().replaceFirst(delimiter, ""))
        binding.symptomTxt.text = finalStr.toString().replaceFirst(delimiter, "")
    }

    fun whenDateClick(view: View) {
        val calendar = Calendar.getInstance()
        val cYear = calendar[Calendar.YEAR]
        val cMonth = calendar[Calendar.MONTH]
        val cDay = calendar[Calendar.DAY_OF_MONTH]
        SpinnerDatePickerDialogBuilder()
            .context(this)
            .callback(this)
            .maxDate(cYear, cMonth, cDay)
            .spinnerTheme(R.style.NumberPickerStyle)
            .defaultDate(cYear, cMonth, cDay)
            .build()
            .show()
//        val listner = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//            val monthOfYear = month + 1
//            val mt: String
//            val dy: String //local variable
//
//            mt = if (monthOfYear < 10) "0$monthOfYear" //if month less than 10 then ad 0 before month
//                else java.lang.String.valueOf(monthOfYear)
//
//            dy = if (dayOfMonth < 10) "0$dayOfMonth" else java.lang.String.valueOf(dayOfMonth)
//
//            whenStartDate.text = "$dy/$mt/$year"
//            DialogUtility.hideProgressDialog()
//            Log.e("when Date: ", " : ${whenStartDate.text}")
//            if (whenStartTime.text.toString() == resources.getString(R.string.time)) {
//                AppHelper.showToast(this, getString(R.string.valid_time_msg))
//            } else {
//                openNextActivity()
//            }
//        }
//        DialogUtility.showDatePickerDialog(this, listner).show()
    }

    fun whenTimeClick(view: View) {
        val listner =
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                binding.whenStartTime.text =
                    "${SimpleDateFormat("hh:mm a").format(SimpleDateFormat("hh:mm").parse("${selectedHour}:${selectedMinute}"))}"
                DialogUtility.hideProgressDialog()
//                Log.e("when Time: ", " : ${whenStartTime.text}")
                if (binding.whenStartDate.text.toString() == resources.getString(R.string.date)) {
                    AppHelper.showToast(this, getString(R.string.valid_date_msg))
                } else {
                    openNextActivity()
                }
            }
        DialogUtility.showTimePickerDialog(this, listner).show()
    }

    private fun openNextActivity() {
        AppSettings.setStringValue(
            this,
            IntentConstants.kASSESSMENT_DATE,
            binding.whenStartDate.text.toString()
        )
        AppSettings.setStringValue(
            this,
            IntentConstants.kASSESSMENT_TIME,
            binding.whenStartTime.text.toString()
        )

        val intent = Intent(this, QuestionDemoActivity::class.java)
        intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptomStr)
        startActivity(intent)
    }

    fun symptomClick(view: View) {
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
        binding.whenStartDate.text = simpleDateFormat.format(calendar.time)
        if (binding.whenStartTime.text.toString() == resources.getString(R.string.time)) {
            AppHelper.showToast(this, getString(R.string.valid_time_msg))
        } else {
            openNextActivity()
        }
    }
}
