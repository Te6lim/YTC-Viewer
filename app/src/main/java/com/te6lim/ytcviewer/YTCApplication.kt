package com.te6lim.ytcviewer

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.network.YtcApi
import com.te6lim.ytcviewer.repository.CardRepository

class YTCApplication : Application() {

    companion object {
        private const val THEME_MODE_KEY = "darkMode"
    }

    private lateinit var preference: SharedPreferences

    val repository: CardRepository by lazy {
        CardRepository(YtcApi.retrofitService, CardDatabase.getInstance(this))
    }

    var toDarkMode = false
        set(value) {
            field = value
            preference.edit().apply {
                putBoolean(THEME_MODE_KEY, value)
            }.apply()
        }

    override fun onCreate() {
        super.onCreate()
        preference = getSharedPreferences(getString(R.string.preferenceFile), Context.MODE_PRIVATE)
        toDarkMode = preference.getBoolean(THEME_MODE_KEY, false)
        if (toDarkMode) AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
    }
}