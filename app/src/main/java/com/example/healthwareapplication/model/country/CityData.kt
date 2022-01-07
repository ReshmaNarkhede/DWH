package com.example.healthwareapplication.model.country

import org.json.JSONObject

class CityData (json: JSONObject){

    private var dataJSONObj: JSONObject? = json

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }
    fun getName(): String? {
        return dataJSONObj!!.optString("name")
    }
    fun getCode(): String? {
        return dataJSONObj!!.optString("code")
    }
    fun getDigits(): String? {
        return dataJSONObj!!.optString("digits")
    }

    fun getCountry(): String? {
        return dataJSONObj!!.optString("country")
    }

    fun getImage(): String? {
        return dataJSONObj!!.optString("image")
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