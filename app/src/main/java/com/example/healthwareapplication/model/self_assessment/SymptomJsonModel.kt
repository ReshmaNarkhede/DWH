package com.example.healthwareapplication.model.self_assessment

import org.json.JSONObject

class SymptomJsonModel(json: JSONObject){

    private var dataJSONObj: JSONObject? = json

    fun getId(): String? {
        return dataJSONObj!!.optString("id")
    }

    fun getName(): String? {
        return dataJSONObj!!.optString("name")
    }

    fun getStatus(): String? {
        return dataJSONObj!!.optString("status")
    }

    fun getBodyPartId(): String? {
        return dataJSONObj!!.optString("bodyPartId")
    }

    fun getUpdatedAt(): String? {
        return dataJSONObj!!.optString("updatedAt")
    }
    fun getCreatedAt(): String? {
        return dataJSONObj!!.optString("createdAt")
    }
}