package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
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
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfig()
    }

    private fun defaultConfig() {
        binding.dobDate.setOnClickListener {
            showDate(R.style.NumberPickerStyle)
        }
        binding.okayBtn.setOnClickListener {
            checkValidation()
        }
        binding.genderTxt.setOnClickListener {
            finish()
        }
        binding.emailEdtTxt.doAfterTextChanged {
            binding.errorText.visibility = View.INVISIBLE
        }
    }

    private fun checkValidation() {
        val email = binding.emailEdtTxt.text.trim().toString()
        val dob = binding.dobDate.text.trim().toString()
        when {
            dob.isEmpty() -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter your date of birth"
            }
            email.isEmpty() -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter your email"
            }
            !email.matches(emailPattern.toRegex())->{
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter your valid email"
            }
            else -> {
                binding.errorText.visibility = View.INVISIBLE
                openNextActivity()
            }
        }
    }

    private fun initComponents() {
        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        userDetailModel =
            intent?.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        binding.welcomeMessage.text = userDetailModel.firstName.plus(getString(R.string.enter_dob))
        if (userDetailModel.sex == "male") {
            binding.genderTxt.text = getString(R.string.male)
        } else {
            binding.genderTxt.text = getString(R.string.female)
        }
    }

    private fun showDate(spinnerTheme: Int) {
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

    private fun openNextActivity() {
        val email = binding.emailEdtTxt.text.toString()
        userDetailModel.email = email
        val intent = Intent(this, RegisterPasswordActivity::class.java)
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
        binding.dobDate.setText(AppHelper.getDobFormat(simpleDateFormat.format(calendar.time)))
    }
}