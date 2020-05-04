package com.example.healthwareapplication.app_utils

import android.content.Context
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.constants.IntentConstants
import com.example.healthwareapplication.model.country.CountryData
import com.example.healthwareapplication.model.self_assessment.SymptomJsonModel
import com.example.healthwareapplication.model.user.UserDetailModel
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject





class AppSessions {

    companion object {

        fun isSession(context: Context): Boolean {
            return AppSettings.getBooleanValue(context, AppConstants.kIS_LOGIN)
        }
        fun getCountry(context: Context): CountryData? {
            val jObj = JSONObject(AppSettings.getJsonObjectValue(context, AppConstants.kCOUNTRY)!!)
            return CountryData(jObj)
        }

        fun getCountryArray(context: Context): JSONArray?{
            return AppSettings.getArrayValue(context,AppConstants.kCOUNTRY_DATA)
        }

        fun getLoginData(context: Context): String? {
            return AppSettings.getJsonObjectValue(context,AppConstants.kLOGIN)
        }

        fun getLoginModel(context: Context): UserDetailModel? {
            val gson = Gson()
            return gson.fromJson(getLoginData(context), UserDetailModel::class.java)
        }

        fun getSymptomData(context: Context): JSONArray? {
            return AppSettings.getArrayValue(context,IntentConstants.kSYMPTOM_DATA)
        }
        fun getQuestionData(context: Context): JSONArray? {
            val array = AppSettings.getArrayValue(context!!, AppConstants.kQUESTION_ARY)
            return if (array.length() > 0) {
                array
            } else {
                JSONArray()
            }
        }
        fun getSymptomModel(context: Context):SymptomJsonModel?{
            val gson = Gson()
            return gson.fromJson(getSymptomData(context).toString(), SymptomJsonModel::class.java)
        }
    }
}