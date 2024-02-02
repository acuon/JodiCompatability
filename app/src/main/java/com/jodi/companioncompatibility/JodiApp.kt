package com.jodi.companioncompatibility

import android.app.Application
import com.example.flavum.data.pref.Preferences
import dagger.hilt.android.HiltAndroidApp

private var appContext: JodiApp? = null

@HiltAndroidApp
class JodiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Preferences.init(this)
    }

    companion object {
        fun getAppContext(): JodiApp {
            return appContext!!
        }
    }
}