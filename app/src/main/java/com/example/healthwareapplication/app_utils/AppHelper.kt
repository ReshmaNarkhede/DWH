package com.example.healthwareapplication.app_utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.healthwareapplication.R
import com.google.gson.JsonObject
import org.json.JSONArray
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AppHelper {
    companion object {
        fun transparentStatusBar(activity: Activity) {
            val window: Window = activity.window
            val winParams: WindowManager.LayoutParams = window.attributes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.TRANSPARENT
            }
            winParams.flags =
                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            window.attributes = winParams
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        }

        fun printParam(msg: String, param: JsonObject) {
            Log.d(msg, ": $param")
        }

        fun printUrl(msg: String, response: Response<JsonObject?>) {
            Log.d(msg, ": " + response.raw().request().url())
        }

        fun printResponse(msg: String, response: Response<JsonObject?>) {
            Log.d(msg, " : " + response.body().toString())
        }

        fun toStringArray(array: JSONArray?): Array<String?>? {
            if (array == null) return null
            val arr = arrayOfNulls<String>(array.length())
            for (i in arr.indices) {
                arr[i] = array.optString(i)
            }
            return arr
        }

        fun getAge(year: String, month: String, day: String): String? {
            val dob: Calendar = Calendar.getInstance()
            val today: Calendar = Calendar.getInstance()
            dob.set(year.toInt(), month.toInt(), day.toInt())
            var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            val ageInt = age
            return ageInt.toString()
        }

        fun getDobFormat(dateStr: String?): String? {
//            val date1 = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(dateStr!!)
            var reformattedStr = ""
            val fromUser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val myFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                reformattedStr = myFormat.format(fromUser.parse(dateStr!!)!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return reformattedStr
        }

        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }
            })
        }

        fun EditText.afterEditorAction(editorAction: (String) -> Unit) {
            this.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_NEXT, EditorInfo.IME_ACTION_PREVIOUS -> {
                        editorAction.invoke(this.text.toString())
                        return@OnEditorActionListener true
                    }
                }
                false
            })
        }
    }
}