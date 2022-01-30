package com.example.healthwareapplication.model.response

import org.json.JSONArray
import org.json.JSONObject


class ResponseModel(response: JSONObject) {

    var resJSONObj:JSONObject? = response

    fun isCode() : Boolean {
        val status = this.resJSONObj?.optInt("code")
        return status == 200
    }

    fun code(): Int? {
        return resJSONObj?.optInt("code")
    }

    fun getMessage() :String?{
        return this.resJSONObj?.optString("message")
    }

    fun getDataArray() : JSONArray? {
        return this.resJSONObj?.optJSONArray("data")
    }
    fun getDataObj() : JSONObject? {
        return this.resJSONObj?.optJSONObject("data")
    }
}