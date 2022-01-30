package com.example.healthwareapplication.views

import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.example.healthwareapplication.R
import com.example.healthwareapplication.databinding.CustomProgressBinding

class ProgressBarDialog {
    lateinit var dialog: Dialog

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog {
        val binding = CustomProgressBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        Glide.with(context).asGif().load(R.drawable.ic_loader).into(binding.progressImage)

//        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflator.inflate(R.layout.custom_progress, null)
//        Glide.with(context).asGif().load(R.drawable.ic_loader).into(view.progressImage)

//        if (title != null) {
//            view.cp_title.text = title
//        }
//        view.cp_bg_view.setBackgroundColor(Color.parseColor("#60000000")) //Background Color
//        view.cp_cardview.setCardBackgroundColor(Color.parseColor("#181717")) //Box Color
//        setColorFilter(
//            view.cp_pbar.indeterminateDrawable,
//            ResourcesCompat.getColor(context.resources, R.color.colorPrimary, null)
//        )
//        view.cp_title.setTextColor(Color.WHITE) //Text Color

        dialog = Dialog(context, R.style.CustomProgressBarTheme)
        dialog.setContentView(binding.root)
        dialog.show()

        return dialog
    }

    fun setColorFilter(@NonNull drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}