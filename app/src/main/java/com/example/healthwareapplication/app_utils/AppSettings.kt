package com.example.healthwareapplication.app_utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray


object AppSettings {

    private const val NAME = "com.example.healthwareapplication"
    private const val MODE = Context.MODE_PRIVATE

    fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME, MODE)
    }

    fun setStringValue(context: Context, key: String, value: String?) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    fun getStringValue(context: Context, key: String): String? {
        return getPrefs(context).getString(key, "")
    }

    fun setIntValue(context: Context, key: String, value: Int) {
        getPrefs(context).edit().putInt(key, value).apply()
    }

    fun getIntValue(context: Context, key: String): Int {
        return getPrefs(context).getInt(key, 0)
    }

    fun setBooleanValue(context: Context, key: String, value: Boolean) {
        getPrefs(context).edit().putBoolean(key, value).apply()
    }

    fun getBooleanValue(context: Context, key: String): Boolean {
        return getPrefs(context).getBoolean(key, false)
    }

    fun setFloatValue(context: Context, key: String, value: Float) {
        getPrefs(context).edit().putFloat(key, value).apply()
    }

    fun getFloatValue(context: Context, key: String): Float {
        return getPrefs(context).getFloat(key, 0.0f)
    }

    fun setLongValue(
        context: Context, key: String, value: Long
    ) {
        getPrefs(context).edit().putLong(key, value).apply()
    }

    fun getLongValue(context: Context, key: String): Long {
        return getPrefs(context).getLong(key, 0)
    }

    fun setJsonObjectValue(context: Context, key: String, value: String) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    fun getJsonObjectValue(context: Context, key: String): String? {
        return getPrefs(context).getString(key, "")
    }

    fun setJsonArrayValue(
        context: Context, key: String, value: String
    ) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    fun getArrayValue(context: Context, key: String): JSONArray {
        val dataString = getPrefs(context).getString(key, "")
        if (dataString!!.isEmpty()) {
            return  JSONArray()
        }
        return  JSONArray(dataString)
    }

    @SuppressLint("CommitPrefEdits")
    fun clearMyPreference(context: Context) {
        getPrefs(context).edit()?.clear()
        getPrefs(context).edit()?.apply()
    }
    fun clearAllData(context: Context) {
        getPrefs(context).edit().clear().commit()
    }
}