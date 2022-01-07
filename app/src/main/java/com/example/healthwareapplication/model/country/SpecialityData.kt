package com.example.healthwareapplication.model.country

import org.json.JSONObject

class SpecialityData(json: JSONObject){
    private var dataJSONObj: JSONObject? = json
    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }
    fun getName(): String? {
        return dataJSONObj!!.optString("name")
    }
}