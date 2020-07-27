package com.example.healthwareapplication.activity.account.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.R.layout.activity_register_terms
import com.example.healthwareapplication.activity.account.OtpActivity
import com.example.healthwareapplication.api.ApiClient
import com.example.healthwareapplication.api.ApiInterface
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_register_terms.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterTermsActivity : AppCompatActivity() {

    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_register_terms)

        initComponents()
        defaultConfiguration()
    }

    private fun initComponents() {
        AppHelper.transparentStatusBar(this)
        userDetailModel = intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
        pwdTxt.text = userDetailModel.password
        pwdTxt.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun defaultConfiguration() {
    }

    fun signupwaaClick(view: View) {
        if (termAndConditionSwitch.isChecked) {
            val userDetailModel =
                intent.getSerializableExtra(IntentConstants.kUSER_DATA) as UserDetailModel
            callRegisterationAPI(userDetailModel)
        } else {
            AppHelper.showToast(this,"Check Term and condition !")
        }
    }

    private fun callRegisterationAPI(userDetailModel: UserDetailModel) {
        val apiService: ApiInterface =
            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)

        val param = JsonObject()
        param.addProperty("username", userDetailModel.email)
        param.addProperty("password", userDetailModel.password)
        param.addProperty("first_name", userDetailModel.firstName)
        param.addProperty("last_name", userDetailModel.lastName)
        param.addProperty("gender", userDetailModel.sex)
        param.addProperty("dob", userDetailModel.dob)
        param.addProperty("tob", userDetailModel.tob)
        param.addProperty("mobile", userDetailModel.mobile)
        param.addProperty("city_id", userDetailModel.cityId)
        param.addProperty("user_type", userDetailModel.userType)
        if (userDetailModel.userType == 1) {
            param.addProperty("speciality", userDetailModel.speciality)
            param.addProperty("experience", userDetailModel.experience)
        }

        val call: Call<JsonObject> = apiService.fetchRegister(param)

        AppHelper.printParam("RegisterParam: ", param)

        call.enqueue(object : Callback<JsonObject?> {

            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {

                if (response.isSuccessful) {
                    val json = JSONObject(response.body().toString())
                    val responseModel = ResponseModel(json)
                    if (responseModel.isCode()) {
                        val data = responseModel.getDataObj()
                        val otp = data!!.optInt("otp")

                        showOTPDialog(otp.toString())
                    } else {
                        AppHelper.showToast(
                            this@RegisterTermsActivity,
                            responseModel.getMessage().toString()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                if (t is NoConnectivityException) {
                    AppHelper.showNetNotAvailable(this@RegisterTermsActivity)
                }
            }
        })
    }

    private fun showOTPDialog(otp: String) {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra(IntentConstants.kOTP, otp)
        intent.putExtra(IntentConstants.kEMAIL, userDetailModel.email)
        intent.putExtra(IntentConstants.kPASSWORD, userDetailModel.password)
        intent.putExtra(IntentConstants.kIS_FORGOT, false)
        startActivity(intent)
    }

    fun termAndConditionClick(view: View) {
        val intent = Intent(this, TermsAndPolicyActivity::class.java)
        startActivityForResult(intent, 205)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 205) {
            if (resultCode == RESULT_OK) {
                termAndConditionSwitch.isChecked = true
            }
        }
    }
}
