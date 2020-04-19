package com.example.healthwareapplication.app_utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.healthwareapplication.R
import com.google.gson.JsonObject
import org.json.JSONArray
import retrofit2.Response


class AppHelper {
    companion object{
        fun transparentStatusBar(activity: Activity) {
            val window: Window = activity.window
            val winParams: WindowManager.LayoutParams = window.attributes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.TRANSPARENT
            }
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            window.attributes = winParams
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        fun showNetNotAvailable(context: Context) {
            Toast.makeText(context, R.string.please_check_internet_connection, Toast.LENGTH_SHORT)
                .show()
        }
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show()
        }
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun printParam(msg: String, param:JsonObject) {
            Log.d(msg,": $param")
        }
        fun printUrl(msg: String, response: Response<JsonObject?>) {
            Log.d(msg,": "+response.raw().request().url())
        }
        fun printResponse(msg: String, response: Response<JsonObject?>) {
            Log.d(msg," : " + response.body().toString())
        }
        fun toStringArray(array: JSONArray?): Array<String?>? {
            if (array == null) return null
            val arr = arrayOfNulls<String>(array.length())
            for (i in arr.indices) {
                arr[i] = array.optString(i)
            }
            return arr
        }
    }
}