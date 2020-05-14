package com.example.healthwareapplication.model.self_assessment

import org.json.JSONObject

class ReportData (json: JSONObject) {
    private var dataJSONObj: JSONObject? = json
    private var userSelectedAnswer: String? = null

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }
    fun getUserId(): String? {
        return dataJSONObj!!.optString("user_id")
    }
    fun getQuestion(): String? {
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
    fun isActive(): String? {
        return dataJSONObj!!.optString("is_active")
    }



//    "id": "3",
//    "user_id": "5",
//    "question_id": "1",
//    "symptoms_id": "6",
//    "answer": " 98.5 - 102 C",
//    "feedback_json": null,
//    "assessment_date": "14/4/2020",
//    "assessment_time": "08:11 pm",
//    "is_active": "1",
//    "created_at": "2020-05-14",
//    "updated_at": "2020-05-14"
}