package com.example.healthwareapplication.app_utils

import android.content.Context
import com.example.healthwareapplication.constants.AppConstants
import com.example.healthwareapplication.model.country.CountryData
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
    }
}