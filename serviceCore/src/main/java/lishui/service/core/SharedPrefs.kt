package lishui.service.core

import android.content.Context
import android.content.SharedPreferences

/**
 * @author lishui.lin
 * Created it on 21-4-20
 */
object SharedPrefs {

    private val appSharedPref: SharedPreferences by lazy {
        val appContext = AppDemo.appContext
        appContext.getSharedPreferences(appContext.packageName, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return appSharedPref.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        appSharedPref.edit().putBoolean(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return appSharedPref.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        appSharedPref.edit().putInt(key, value).apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return appSharedPref.getLong(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        appSharedPref.edit().putLong(key, value).apply()
    }

    fun getString(key: String, defaultValue: String?): String? {
        return appSharedPref.getString(key, defaultValue)
    }

    fun putString(key: String, value: String?) {
        appSharedPref.edit().putString(key, value).apply()
    }

    fun putStringSet(key: String, value: Set<String?>?) {
        appSharedPref.edit().putStringSet(key, value).apply()
    }

    fun getStringSet(
        key: String, defaultValue: Set<String?>?
    ): Set<String>? {
        return appSharedPref.getStringSet(key, defaultValue)
    }

    fun getAll(): Map<String, *> {
        return appSharedPref.all
    }

    fun remove(key: String) {
        appSharedPref.edit().remove(key).apply()
    }

    fun registerListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        appSharedPref.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) {
        appSharedPref.unregisterOnSharedPreferenceChangeListener(listener)
    }

}