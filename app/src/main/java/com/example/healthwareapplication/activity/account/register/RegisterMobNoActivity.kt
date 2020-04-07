package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.healthwareapplication.R
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.country.CountryData
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_register_mob_no.*
import kotlinx.android.synthetic.main.recycler_country_item.view.*

class RegisterMobNoActivity : AppCompatActivity() {
    private lateinit var mobileNo: TextInputEditText
    private lateinit var population: TextView
    private lateinit var populationMessage: TextView
    private lateinit var hdi: TextView
    private lateinit var hdiMessage: TextView
    private lateinit var bmi: TextView
    private lateinit var bmiMessage: TextView
    private lateinit var countryCode: TextView
    private lateinit var countryImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_mob_no)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        mobileNo = findViewById(R.id.mobileNo)
        population = findViewById(R.id.population)
        populationMessage = findViewById(R.id.populationMessage)
        hdi = findViewById(R.id.hdi)
        hdiMessage = findViewById(R.id.hdiMessage)
        bmi = findViewById(R.id.bmi)
        bmiMessage = findViewById(R.id.bmiMessage)
        countryCode = findViewById(R.id.countryCode)
        countryImage = findViewById(R.id.countryImage)
    }

    private fun defaultConfiguration() {
        val countryModel = AppSessions.getCountry(this)!!

        val populationValue = countryModel.getPopulation()!!.split(" ")
        val hdiValue = countryModel.getHdi()!!.split(" ")
        val mdiValue = countryModel.getMdi()!!.split(" ")
        if (populationValue.size > 1) {
            population.text = populationValue[0]
            populationMessage.text = " " + populationValue[1] + "" + populationMessage.text.toString()
        } else {
            population.text = populationValue[0]
        }
        if (hdiValue.size > 1) {
           hdi.text = hdiValue[0]
            hdiMessage.text = " " + hdiValue[1] + "" + hdiMessage.text.toString()
        } else {
            hdi.text = hdiValue[0]
        }
        if (mdiValue.size > 1) {
            bmi.text = mdiValue[0]
            bmiMessage.text = " " + mdiValue[1] + "" + bmiMessage.text.toString()
        } else {
            bmi.text = mdiValue[0]
        }

        Glide.with(countryImage).load(countryModel.getImage())
            .apply(RequestOptions().placeholder(R.drawable.dwh_logo)).into(countryImage)

        countryCode.text = countryModel.getCode()
    }



    fun thanksClick(view: View) {
        if (mobileNo.text.toString() == "") {
            mobileNo.error = resources.getString(R.string.errormessage)
        } else {
            val userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
            userDetailModel.mobile = mobileNo.text.toString()
            val intent = Intent(this, RegisterAgeActivity::class.java)
            intent.putExtra(IntentConstants.kUSER_DATA, userDetailModel)
            startActivity(intent)
        }
    }
}
