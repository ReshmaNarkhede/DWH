package com.example.healthwareapplication.model.self_assessment

import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class QuestionData(json: JSONObject) {

    private var dataObj: JSONObject? = json

    fun getQuestionAns(): JSONArray? {
        return dataObj!!.optJSONArray("question_ans")
    }

    class QuestionAnsModel(json: JSONObject) {
        private var dataJSONObj: JSONObject? = json
        private var userSelectedAnswer: String? = null

        fun getSelectedAnswer(): String? {
            userSelectedAnswer = dataJSONObj!!.optString("selected_answer")
            return userSelectedAnswer
        }

        fun getId(): String? {
            return dataJSONObj!!.optString("id")
        }

        fun getSymptomId(): String? {
            return dataJSONObj!!.optString("symptoms_id")
        }

        fun getQuestion(): String? {
            return dataJSONObj!!.optString("question")
        }

        fun getGroupID(): Int? {
            return dataJSONObj!!.optInt("group_id")
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

    fun parseGroupedQuestionArray(productList: JSONArray?): JSONArray? {
        val groupedProductArray = JSONArray()
        if (null != productList) {
            val productCount = productList.length()
            var currentMarketId: Int = -1
            var productArray: JSONArray? = null
            for (i in 0 until productCount) {
                val jsonObj: JSONObject? = productList.getJSONObject(i)
                val model = QuestionAnsModel(jsonObj!!)
                if (currentMarketId != model.getGroupID()) {
                    currentMarketId = model.getGroupID()!!
                    if (null != productArray) {
                        groupedProductArray.put(productArray)
                    }
                    productArray = JSONArray()
                }
                try {
                    productArray!!.put(jsonObj)
                } catch (e: JSONException) {
                    Log.e("EXCEPTION", e.toString())
                }
            }
            if (null != productArray) {
                groupedProductArray.put(productArray)
            }
        }
        return groupedProductArray
    }
}