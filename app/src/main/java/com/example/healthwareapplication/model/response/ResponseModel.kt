package app.frats.android.models.response

import org.json.JSONArray
import org.json.JSONObject


class ResponseModel(response: JSONObject) {

    var resJSONObj:JSONObject? = response

    fun isCode() : Boolean {
        val status = this.resJSONObj?.optInt("code")
        return status == 200
    }

    fun code() : Int {
        val status = this.resJSONObj?.optInt("code")
        return status!!
    }

    fun getMessage() :String?{
        return this.resJSONObj?.optString("message")
    }

    fun getDataArray() : JSONArray? {
        return this.resJSONObj!!.optJSONArray("detail")
    }
    fun getDataObj() : JSONObject? {
        return this.resJSONObj!!.optJSONObject("detail")!!
    }


}