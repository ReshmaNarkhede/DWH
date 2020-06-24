package com.example.healthwareapplication.activity.account.register

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            Toast.makeText(this, "Check Term and condition !", Toast.LENGTH_LONG).show()
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
        param.addProperty("gender", userDetailModel.gender)
        param.addProperty("dob", userDetailModel.dob)
        param.addProperty("tob", userDetailModel.tob)
        param.addProperty("mobile", userDetailModel.mobile)
        param.addProperty("city_id", userDetailModel.cityId)
        param.addProperty("user_type", userDetailModel.userType)
        if(userDetailModel.userType==1){
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
                        AppHelper.showToast(this@RegisterTermsActivity, responseModel.getMessage().toString())
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
        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Atenção")
        builder.setMessage("Your Otp is: $otp")
        builder.setPositiveButton("okay") { dialog, which ->
            val intent = Intent(this,OtpActivity::class.java)
            intent.putExtra(IntentConstants.kOTP,otp)
            intent.putExtra(IntentConstants.kEMAIL,userDetailModel.email)
            intent.putExtra(IntentConstants.kIS_FORGOT,false)
            startActivity(intent)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun termAndConditionClick(view: View) {
        termAndConditionDetailLayout.visibility = View.VISIBLE
        termAndConditionLayout.visibility = View.GONE
    }

    fun termAndConditionReadClick(view: View) {
        termAndConditionDetailLayout.visibility = View.GONE
        termAndConditionLayout.visibility = View.VISIBLE
        termAndConditionSwitch.isChecked = true
    }


//    private fun callRegisteration(userDetailModel: UserDetailModel) {
////        val apiService: ApiInterface =
////            ApiClient.getRetrofitClient(this)!!.create(ApiInterface::class.java)
////
////        val fieldsmap = HashMap<String, RequestBody>()
////        fieldsmap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.mobile))
////        fieldsmap.put("firstName", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.firstName))
////        fieldsmap.put("lastName", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.lastName))
////        fieldsmap.put("dob", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.dob))
////        fieldsmap.put("tob", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.tob))
////        fieldsmap.put("sex", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.gender))
////        fieldsmap.put("countryId", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.countryId))
////        fieldsmap.put("password", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.password))
////        fieldsmap.put("userType", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.userType.toString()))
////        fieldsmap.put("parentId", RequestBody.create(MediaType.parse("text/plain"), userDetailModel.parentId))
////        if (userDetailModel.resume != "") {
////            val file = File(userDetailModel.resume)
////            fieldsmap["resume\"; filename=" + userDetailModel.resumeName + "." + file.extension] = RequestBody.create(
////                MediaType.parse("image/*"), file)
////        }
////        fieldsmap["mciNo"] = RequestBody.create(MediaType.parse("text/plain"), userDetailModel.mciNo)
////
////        Log.d("map: ",": $fieldsmap")
////        val call: Call<JsonObject> = apiService.registration(fieldsmap)
////        DialogUtility.showProgressDialog(this)
////        call.enqueue(object : Callback<JsonObject?> {
////
////            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
////
////                Log.e("REGISTER: ",": "+response.raw().request().url())
////                if (response.isSuccessful) {
////                    DialogUtility.hideProgressDialog()
////                    val json = JSONObject(response.body().toString())
////                    val responseModel = ResponseModel(json)
////                    if (responseModel.isCode()) {
////                        AppHelper.showToast(this@RegisterTermsActivity,responseModel.getMessage().toString())
////                        val intent = Intent(this@RegisterTermsActivity, OldLoginActivity::class.java)
////                        startActivity(intent)
////                     }
////                    else{
////                        AppHelper.showToast(this@RegisterTermsActivity,responseModel.getMessage().toString())
////                    }
////                }
////            }
////
////            override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
////                if (t is NoConnectivityException) {
////                    DialogUtility.hideProgressDialog()
////                    AppHelper.showNetNotAvailable(this@RegisterTermsActivity)
////                }
////            }
////        })
////    }
}
