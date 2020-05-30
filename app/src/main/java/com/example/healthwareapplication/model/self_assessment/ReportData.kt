package com.example.healthwareapplication.model.self_assessment

import org.json.JSONObject

class ReportData (json: JSONObject) {
    private var dataJSONObj: JSONObject? = json

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }
    fun getAssessmentReportId(): String? {
        return dataJSONObj!!.optString("self_assessment_report_id")
    }
    fun getQuestionId(): String? {
        return dataJSONObj!!.optString("question_id")
    }
    fun getSymptomId(): String? {
        return dataJSONObj!!.optString("symptoms_id")
    }
    fun getAnswer(): String? {
       return dataJSONObj!!.optString("answer")
    }
    fun getAssessmentDate(): String? {
        return dataJSONObj!!.optString("assessment_date")
    }
    fun getAssessmentTime(): String? {
        return dataJSONObj!!.optString("assessment_time")
    }
    fun getQuestion(): String? {
        return dataJSONObj!!.optString("question")
    }
}