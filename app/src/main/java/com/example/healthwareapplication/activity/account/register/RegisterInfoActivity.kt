package com.example.healthwareapplication.activity.account.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.databinding.ActivityRegisterInfoBinding
import com.example.healthwareapplication.model.country.CountryData
import com.example.healthwareapplication.model.user.UserDetailModel
import org.json.JSONObject

class RegisterInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterInfoBinding
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        binding.ageTxt.setOnClickListener {
            finish()
        }
    }

    private fun defaultConfiguration() {
        binding.okayBtn.setOnClickListener {
            checkValidation()
        }
        val countryForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val countrySelected = intent?.getStringExtra(IntentConstants.kCOUNTRY_SELECTED)
                if(countrySelected != null){
                    val jsonObject = JSONObject(countrySelected)
                    val country = CountryData(jsonObject)
                    userDetailModel.countryId = country.getId()
                    binding.countryTxt.setText(country.getCountry())
                }
            }
        }
        val cityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val citySelected = intent?.getStringExtra(IntentConstants.kCITY_SELECTED)
                if(citySelected != null){
                    val jsonObject = JSONObject(citySelected)
                    val city = CountryData(jsonObject)
                    userDetailModel.cityId = city.getId()?.let { Integer.parseInt(it) }
                    binding.cityTxt.setText(city.getName())
                }
            }
        }
        binding.countryTxt.setOnClickListener {
            countryForResult.launch(Intent(this,CountrySearchActivity::class.java))
        }
        binding.cityTxt.setOnClickListener {
            if(binding.countryTxt.text.isNotEmpty()) {
                binding.errorText.visibility = View.INVISIBLE
                val country = binding.countryTxt.text.toString()
                val intent =Intent(this, CitySearchActivity::class.java)
                intent.putExtra(IntentConstants.kCOUNTRY_SELECTED,country)
                cityForResult.launch(intent)
            }else{
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please select your country"
            }
        }

        binding.nameEdtTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()) {
                    if (userDetailModel.userType == 1) {
                        binding.drPrefix.visibility = View.VISIBLE
                    } else {
                        binding.drPrefix.visibility = View.GONE
                    }
                } else {
                    binding.drPrefix.visibility = View.GONE
                }
            }
        })
    }

    private fun checkValidation() {
        val name = binding.nameEdtTxt.text.trim().toString()
        val country = binding.countryTxt.text.trim().toString()
        val city = binding.cityTxt.text.trim().toString()
        when {
            name.isEmpty() -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please enter your name"
            }
            country.isEmpty() -> {
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please select your country & city name"
            }
            city.isEmpty() ->{
                binding.errorText.visibility=View.VISIBLE
                binding.errorText.text = "Please select your city name"
            }
            else -> {
                binding.errorText.visibility = View.INVISIBLE
                goToNext()
            }
        }

    }

    private fun goToNext() {
        userDetailModel.firstName = binding.nameEdtTxt.text.toString()
        userDetailModel.countryName = binding.countryTxt.text.toString()
        userDetailModel.cityName = binding.cityTxt.text.toString()

        if (userDetailModel.userType == 1) {
            val intent = Intent(this, DoctorSpecialityActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        } else if (userDetailModel.userType == 2) {
            val intent = Intent(this, GenderActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        }
    }

}
