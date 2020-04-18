package com.example.healthwareapplication.model.self_assessment

import org.json.JSONArray
import org.json.JSONObject

class QuestionData(json: JSONObject) {

    private var selected = false
    private var dataJSONObj: JSONObject? = json
    fun isSelected(): Boolean {
        return selected
    }

    fun setSelected(selected: Boolean) {
        this.selected = selected
    }

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }

    fun getSymptomId(): String? {
        return dataJSONObj!!.optString("symptoms_id")
    }

    fun getQuestion(): String? {
        return dataJSONObj!!.optString("name")
    }

    fun getQuestionType(): String? {
        return dataJSONObj!!.optString("type")
    }

    fun isSubQuestion(): String? {
        return dataJSONObj!!.optString("is_sub_question")
    }

    fun getStatus(): String? {
        return dataJSONObj!!.optString("status")
    }

    fun getUpdatedAt(): String? {
        return dataJSONObj!!.optString("created_at")
    }

    fun getCreatedAt(): String? {
        return dataJSONObj!!.optString("created_at")
    }

    fun getAnswers(): JSONArray? {
        return dataJSONObj!!.optJSONArray("answers")
    }

    class AnswerData(json: JSONObject?) {
        private var dataObj: JSONObject? = json
        fun getAnswerId(): String? {
            return dataObj!!.optString("id")
        }

        fun getQuestionId(): String? {
            return dataObj!!.optString("question_id")
        }

        fun getAnswerValue(): String? {
            return dataObj!!.optString("name")
        }

        fun getStatus(): String? {
            return dataObj!!.optString("status")
        }

        fun getUpdatedAt(): String? {
            return dataObj!!.optString("created_at")
        }

        fun getCreatedAt(): String? {
            return dataObj!!.optString("created_at")
        }
    }

    fun getSubQuestions(): JSONArray? {
        return dataJSONObj!!.optJSONArray("sub_question")
    }

    class SubQuestionData(json: JSONObject?) {
        private var quesDataJSONObj: JSONObject? = json

        fun getSubQuestionId(): String? {
            return quesDataJSONObj!!.optString("id")
        }

        fun getQuestionId(): String? {
            return quesDataJSONObj!!.optString("question_id")
        }

        fun getSubQuestion(): String? {
            return quesDataJSONObj!!.optString("name")
        }

        fun getSubQuestionType(): String? {
            return quesDataJSONObj!!.optString("type")
        }

        fun isSubQuestion(): String? {
            return quesDataJSONObj!!.optString("is_sub_question")
        }

        fun getSubQuestionStatus(): String? {
            return quesDataJSONObj!!.optString("status")
        }

        fun getUpdatedAt(): String? {
            return quesDataJSONObj!!.optString("created_at")
        }

        fun getCreatedAt(): String? {
            return quesDataJSONObj!!.optString("created_at")
        }

        fun getSubQuestionAnswers(): JSONArray? {
            return quesDataJSONObj!!.optJSONArray("sub_answers")
        }

        class SubQuestionAnswerData(json: JSONObject?) {
            private var dataObj: JSONObject? = json
            fun getSubQuesAnswerId(): String? {
                return dataObj!!.optString("id")
            }

            fun getSubQuestionId(): String? {
                return dataObj!!.optString("sub_question_id")
            }

            fun getSubQuesAnswerValue(): String? {
                return dataObj!!.optString("name")
            }

            fun getStatus(): String? {
                return dataObj!!.optString("status")
            }

            fun getUpdatedAt(): String? {
                return dataObj!!.optString("created_at")
            }

            fun getCreatedAt(): String? {
                return dataObj!!.optString("created_at")
            }
        }
    }
}