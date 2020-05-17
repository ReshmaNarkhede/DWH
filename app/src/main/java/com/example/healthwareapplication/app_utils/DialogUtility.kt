package com.example.healthwareapplication.app_utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.ProgressBar
import com.example.healthwareapplication.R
import java.util.*


class DialogUtility {
    companion object{
        var dialog: Dialog? = null
        fun showProgressDialog(context: Context) {
            if (dialog == null) {
                dialog = Dialog(context)
                dialog?.setContentView(ProgressBar(context))
                dialog!!.setCanceledOnTouchOutside(false)
            }
            if (!(context is Activity && context.isFinishing)) {
                dialog?.show()
            }

        }

        fun hideProgressDialog() {
            if (dialog != null && dialog?.isShowing!!) {
                dialog?.dismiss()
                dialog = null
            }
        }

        fun showDatePickerDialog(context: Context, listner: DatePickerDialog.OnDateSetListener): Dialog {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(context, R.style.DialogTheme, listner, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            dialog = datePickerDialog
            return dialog as DatePickerDialog
        }

        fun showTimePickerDialog(context: Context, listner: TimePickerDialog.OnTimeSetListener): Dialog {

            val calendar = Calendar.getInstance()
            dialog = TimePickerDialog(context, R.style.DialogTheme, listner, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                Calendar.MINUTE), false)
            return dialog as TimePickerDialog
        }
    }
}