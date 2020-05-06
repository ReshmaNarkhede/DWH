package com.example.healthwareapplication.activity.self_assessment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.DialogUtility
import com.example.healthwareapplication.constants.IntentConstants
import org.json.JSONArray
import java.text.SimpleDateFormat

class WhenStartActivity : AppCompatActivity() {

    private var symptomStr: String? = null
    private lateinit var whenStartTime: TextView
    private lateinit var whenStartDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_when_start)

        initComponents()
        defaultConfiguration()
    }

    private fun defaultConfiguration() {
        symptomStr = intent.getStringExtra(IntentConstants.kSYMPTOM_DATA)
    }

    private fun initComponents() {
        whenStartTime = findViewById(R.id.assessmentStartTime)
        whenStartDate = findViewById(R.id.assessmentStartDate)
    }

    fun clickNext(view: View) {
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra(IntentConstants.kSYMPTOM_DATA, symptomStr)
        startActivity(intent)
        finish()
    }

    fun whenDateClick(view: View) {
        val listner = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            whenStartDate.text = "$dayOfMonth/$monthOfYear/$year"
            DialogUtility.hideProgressDialog()
        }
        DialogUtility.showDatePickerDialog(this, listner).show()
    }

    fun whenTimeClick(view: View) {
        val listner =
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                whenStartTime.text =
                    "${SimpleDateFormat("hh:mm a").format(SimpleDateFormat("hh:mm").parse("${selectedHour}:${selectedMinute}"))}"
                DialogUtility.hideProgressDialog()
            }
        DialogUtility.showTimePickerDialog(this, listner).show()
    }
}
