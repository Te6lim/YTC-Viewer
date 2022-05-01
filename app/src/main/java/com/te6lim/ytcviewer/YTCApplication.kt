package com.te6lim.ytcviewer

import android.app.Application
import kotlin.properties.Delegates

class YTCApplication : Application() {

    var toDarkMode by Delegates.notNull<Boolean>()

    override fun onCreate() {
        super.onCreate()
        toDarkMode = false
    }
}