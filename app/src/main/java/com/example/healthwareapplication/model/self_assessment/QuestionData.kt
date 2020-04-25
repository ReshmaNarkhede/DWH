package com.example.healthwareapplication.model.self_assessment

import org.json.JSONArray
import org.json.JSONObject

class QuestionData(json: JSONObject) {

    private var dataObj: JSONObject? = json

    fun getQuestionAns():JSONArray?{
        return dataObj!!.optJSONArray("question_ans")
    }

    class QuestionAnsModel(json: JSONObject) {
        private var dataJSONObj: JSONObject? = json

        fun getId(): String? {
            return dataJSONObj!!.optString("id")
        }

        fun getSymptomId(): String? {
            return dataJSONObj!!.optString("symptoms_id")
        }

        fun getQuestion(): String? {
            return dataJSONObj!!.optString("question")
        }

        fun getGroupID(): String? {
            return dataJSONObj!!.optString("group_id")
        }

        fun getAnswer(): String? {
            return dataJSONObj!!.optString("answer")
        }

        fun getAnswerOptions(): String {
            return dataJSONObj!!.optString("ans_options")
        }

        fun getQuestionType(): String? {
            return dataJSONObj!!.optString("ans_type")
        }

        fun getReportGenerate(): String? {
            return dataJSONObj!!.optString("report_generate")
        }

        fun isActive(): String? {
            return dataJSONObj!!.optString("is_active")
        }

        fun getUpdatedAt(): String? {
            return dataJSONObj!!.optString("created_at")
        }

        fun getCreatedAt(): String? {
            return dataJSONObj!!.optString("created_at")
        }
    }
}