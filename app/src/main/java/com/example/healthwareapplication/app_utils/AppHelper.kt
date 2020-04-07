package com.example.healthwareapplication.app_utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.healthwareapplication.R

class AppHelper {
    companion object{
        fun transparentStatusBar(activity: Activity) {
            val window: Window = activity.window
            val winParams: WindowManager.LayoutParams = window.attributes
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
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}