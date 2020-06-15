package com.example.healthwareapplication.api

import android.content.Context
import android.util.Log
import app.frats.android.models.response.ResponseModel
import com.example.healthwareapplication.app_utils.AppHelper
import com.example.healthwareapplication.app_utils.AppSessions
import com.example.healthwareapplication.app_utils.AppSettings
import com.example.healthwareapplication.app_utils.NoConnectivityException
import com.example.healthwareapplication.constants.AppConstants
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
    }
}