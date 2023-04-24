package com.azercell.cardsystem.data.managers

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import java.lang.ref.WeakReference
import java.util.*

class UserManager(context: Context) {

    private val mContext = WeakReference(context)

    fun validatePin(pin: String): Boolean {
        val p = getPin()
        return !(p.isBlank() || p != pin)
    }

    //TODO for testing purpose this method is public
    fun getPin(): String {
        return "1111"
    }

    fun getLanguage(): String? {
        return PreferenceManager.getDefaultSharedPreferences(mContext.get()!!)
            .getString(LANGUAGE, null)
    }

    fun getDefaultLanguage(): String {
        return "az"
    }

    fun setLanguage(language: String) {
        PreferenceManager.getDefaultSharedPreferences(mContext.get()!!).edit {
            putString(LANGUAGE, language)
        }
    }

    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    companion object {
        private const val LANGUAGE = "language"
    }
}