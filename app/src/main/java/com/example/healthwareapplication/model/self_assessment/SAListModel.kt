package com.example.healthwareapplication.model.self_assessment

import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat

class SAListModel(json: JSONObject) {
    private var dataJSONObj: JSONObject? = json

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }
    fun getUserId(): String? {
        return dataJSONObj!!.optString("user_id")
    }
    fun getReportDate(): String? {
//        17/4/2020
        val dateStr = dataJSONObj!!.optString("report_date")
        val inFormat = SimpleDateFormat("dd/mm/yyyy")
        val date = inFormat.parse(dateStr)
        val outFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        return outFormat.format(date!!)
    }
    fun getReportTime(): String? {
        return dataJSONObj!!.optString("report_time")
    }
    fun getSymptom(): String? {
        return dataJSONObj!!.optString("symptom")
    }

}