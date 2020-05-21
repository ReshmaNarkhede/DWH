package com.example.healthwareapplication.app_utils

import android.app.Application

class OverrideTypefaceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        overrideDefaultTypefaces()
    }

    /**
     * Method used to override the default typefaces with the custom fonts
     * for the application.
     */
    private fun overrideDefaultTypefaces() {
        FontChanger.overrideDefaultFont(this, "DEFAULT", "fonts/montserrat_light.ttf")
        FontChanger.overrideDefaultFont(this, "MONOSPACE", "fonts/montserrat_light.ttf")
        FontChanger.overrideDefaultFont(this, "SERIF", "fonts/PTF55F.ttf")
        FontChanger.overrideDefaultFont(this, "SANS_SERIF", "fonts/montserrat_light.ttf")
    }
}