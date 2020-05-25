package com.example.healthwareapplication.activity.self_assessment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.R.layout.activity_when_start
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import kotlinx.android.synthetic.main.activity_when_start.*
import org.json.JSONArray
import java.text.SimpleDateFormat

class WhenStartActivity : AppCompatActivity() {

    private var symptomStr: String? = null
    val delimiter = ","
    val finalStr = SpannableStringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_when_start)

        defaultConfiguration()
    }


    private fun defaultConfiguration() {
        symptomStr = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
        val symptmJsonAry = JSONArray(symptomStr)
        for (i in 0 until symptmJsonAry.length()) {
            val obj = SymptomJsonModel(symptmJsonAry.getJSONObject(i))
            finalStr.append(delimiter)
            finalStr.append(obj.getName())
        }
        Log.e("STr: ", ": " + finalStr.toString().replaceFirst(delimiter, ""))
        symptomTxt.text = finalStr.toString().replaceFirst(delimiter, "")
    }

    fun whenDateClick(view: View) {
        val listner = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            whenStartDate.text = "$dayOfMonth/$monthOfYear/$year"
            DialogUtility.hideProgressDialog()
            Log.e("Time: ", " : ${whenStartTime.text}")
            if (whenStartTime.text.toString() == resources.getString(R.string.time)) {
                AppHelper.showToast(this, "Please select the time.")
            } else {
                openNextActivity()
            }
        }
        DialogUtility.showDatePickerDialog(this, listner).show()
    }

    fun whenTimeClick(view: View) {
        val listner =
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                whenStartTime.text =
                    "${SimpleDateFormat("hh:mm a").format(SimpleDateFormat("hh:mm").parse("${selectedHour}:${selectedMinute}"))}"
                DialogUtility.hideProgressDialog()
                Log.e("Time: ", " : ${whenStartDate.text}")
                if (whenStartDate.text.toString() == resources.getString(R.string.date)) {
                    AppHelper.showToast(this, "Please select the date.")
                } else {
                    openNextActivity()
                }
            }
        DialogUtility.showTimePickerDialog(this, listner).show()
    }

    private fun openNextActivity() {
        AppSettings.setStringValue(this,IntentConstants.kASSESSMENT_DATE,whenStartDate.text.toString())
        AppSettings.setStringValue(this,IntentConstants.kASSESSMENT_TIME,whenStartTime.text.toString())

        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptomStr)
        startActivity(intent)
    }

    fun symptomClick(view: View) {
        finish()
    }
}
