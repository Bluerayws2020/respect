package com.blueray.respect_new

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.onesignal.OneSignal
const val ONE_SIGNAL_ID = "5b0743b7-6b0e-48fa-b474-6dd4c531d48b"
class AppClass : Application() {

    companion object {
        lateinit var context: Context

    }
    override fun onCreate() {
        super.onCreate()

        context =this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONE_SIGNAL_ID)

        OneSignal.promptLocation()
    }
}