package com.example.healthwareapplication.app_utils

import android.content.Context
import android.graphics.Typeface
import java.lang.reflect.Field

class FontChanger {
    companion object {
        fun overrideDefaultFont(
            context: Context,
            staticTypefaceFieldName: String, fontAssetName: String?
        ) {
            val regular = Typeface.createFromAsset(
                context.assets,
                fontAssetName
            )
            replaceFont(staticTypefaceFieldName, regular)
        }

        /**
         * This method uses reflection to access the typeface information
         * and then override the same.
         */
        private fun replaceFont(
            staticTypefaceFieldName: String,
            newTypeface: Typeface
        ) {
            try {
                val staticField: Field = Typeface::class.java
                    .getDeclaredField(staticTypefaceFieldName)
                staticField.isAccessible = true
                staticField.set(null, newTypeface)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
}