package com.example.healthwareapplication.model.self_assessment

import android.util.Log
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
        val dateStr = dataJSONObj!!.optString("report_date")
        Log.e("DAte: ",": $dateStr")
//        val inFormat = SimpleDateFormat("dd/mm/yyyy")
//        val date = inFormat.parse(dateStr)
//        Log.e("DAte show: ", ": $date")
//        val outFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
//        return outFormat.format(date!!)

        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date = formatter.parse(dateStr)
        val desiredFormat = SimpleDateFormat("dd MMM yyyy").format(date!!)
        return desiredFormat
    }
    fun getReportTime(): String? {
        return dataJSONObj!!.optString("report_time")
    }
    fun getSymptom(): String? {
        return dataJSONObj!!.optString("symptom")
    }
    fun getSymptomId(): String? {
        return dataJSONObj!!.optString("symptom_id")
    }
}