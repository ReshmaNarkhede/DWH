package com.example.healthwareapplication.model.self_assessment

import org.json.JSONObject

class SAListModel(json: JSONObject) {
    private var dataJSONObj: JSONObject? = json

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }
    fun getUserId(): String? {
        return dataJSONObj!!.optString("user_id")
    }
    fun getReportDate(): String? {
        return dataJSONObj!!.optString("report_date")
    }
    fun getReportTime(): String? {
        return dataJSONObj!!.optString("report_time")
    }
    fun getSymptom(): String? {
        return dataJSONObj!!.optString("symptom")
    }

}