package com.blueray.respect_new

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class AppClass : Application() {

    companion object {
        lateinit var context: Context

    }
    override fun onCreate() {
        super.onCreate()

        context =this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
//
//        OneSignal.initWithContext(this)
//        OneSignal.setAppId(ONESIGNAL_APP_ID)
//
//        OneSignal.promptForPushNotifications();
//        OneSignal.promptLocation()
    }
}