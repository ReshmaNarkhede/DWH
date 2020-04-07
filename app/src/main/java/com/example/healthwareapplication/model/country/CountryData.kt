package com.example.healthwareapplication.model.country

import app.frats.android.api.APIRequests
import org.json.JSONObject
import java.io.Serializable

class CountryData(json: JSONObject){
    private var dataJSONObj: JSONObject? = json

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }

    fun getUniqueIdName(): String? {
        return dataJSONObj!!.optString("uniqueIdName")
    }

    fun getUniqueIdLength(): String? {
        return dataJSONObj!!.optString("uniqueIdLength")
    }

    fun getName(): String? {
        return dataJSONObj!!.optString("name")
    }
    fun getCode(): String? {
        return dataJSONObj!!.optString("code")
    }
    fun getImage(): String? {
        return APIRequests.kIMAGE_URL+ dataJSONObj!!.optString("image")
    }

    fun getPopulation(): String? {
        return dataJSONObj!!.optString("population")
    }

    fun getHdi(): String? {
        return dataJSONObj!!.optString("hdi")
    }
    fun getMdi(): String? {
        return dataJSONObj!!.optString("mdi")
    }
    fun getCountry2code(): String? {
        return dataJSONObj!!.optString("country_2_code")
    }
}