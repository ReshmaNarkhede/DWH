package com.example.healthwareapplication.api

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.activity.account.OtpActivity
import com.example.healthwareapplication.app_utils.*
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiData {
    companion object{
        private fun fetchList(context: Context,pageCount: Int?) {
            Log.e("Count: ",": $pageCount")
            val apiService: ApiInterface =
                ApiClient.getRetrofitClient(context)!!.create(ApiInterface::class.java)

            val param = JsonObject()
            param.addProperty("user_id", AppSessions.getUserId(context))
            param.addProperty("page", pageCount)

            AppHelper.printParam("list param:", param)

            val call: Call<JsonObject> = apiService.getSAList(param)
            call.enqueue(object : Callback<JsonObject?> {

                override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                    AppHelper.printUrl("list URL:", response)

                    if (response.isSuccessful) {
                        AppHelper.printResponse("list REs:", response)

                        val json = JSONObject(response.body().toString())
                        val responseModel = ResponseModel(json)
                        if (responseModel.isCode()) {
                            val listAry = responseModel.getDataArray()!!
                            AppSettings.setStringValue(context, AppConstants.kASSESSMENT_DATA, listAry.toString())
                        } else {
                            AppHelper.showToast(context, responseModel.getMessage().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                    if (t is NoConnectivityException) {
                        AppHelper.showNetNotAvailable(context)
                    }
                }
            })
        }
       fun fetchForgotPwdAPI(context: Context,email: String) {
            val apiService: ApiInterface = ApiClient.getRetrofitClient(context)!!.create(ApiInterface::class.java)

            val param = JsonObject()
            param.addProperty("email_id", email)

            val call: Call<JsonObject> = apiService.forgotPassword(param)

            call.enqueue(object : Callback<JsonObject?> {

                override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                    if (response.isSuccessful) {
                        DialogUtility.hideProgressDialog()
                        val json = JSONObject(response.body().toString())
                        val responseModel = ResponseModel(json)
//                    if (responseModel.isCode()) {
                        val data = responseModel.getDataObj()
                        val otp = data!!.optInt("otp")
                        showOTPDialog(context,email,otp.toString())
//                    }
//                    else{
//                        AppHelper.showToast(this@ForgotPasswordActivity,responseModel.getMessage().toString())
//                    }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>?, t: Throwable) {
                    if (t is NoConnectivityException) {
                        AppHelper.showNetNotAvailable(context)
                    }
                }
            })
        }
        private fun showOTPDialog(context: Context, email: String, otp: String) {
            val builder = AlertDialog.Builder(context)
//        builder.setTitle("Atenção")
            builder.setMessage("Your Otp is: $otp")
            builder.setPositiveButton("okay") { dialog, which ->
                val intent = Intent(context, OtpActivity::class.java)
                intent.putExtra(IntentConstants.kOTP,otp)
                intent.putExtra(IntentConstants.kEMAIL,email)
                intent.putExtra(IntentConstants.kIS_FORGOT,true)
                context.startActivity(intent)
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}