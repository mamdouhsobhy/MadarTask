package com.madar.madartask.core.presentation.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.madar.madartask.madarTask.data.ModelUserData

@Suppress("UNCHECKED_CAST")
class SharedPrefs(context: Context) {
    companion object {
        private const val PREF = "MyAppPrefName"
        private const val PREF_TOKEN = "user_token"
        private const val PREF_FIREBASE_TOKEN = "user_firebase_token"
        private const val USER_DATA = "USER_DATA"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)


    fun saveToken(token: String) {
        put(PREF_TOKEN, token)
    }

    fun getToken(): String {
        return get(PREF_TOKEN, String::class.java)
    }

    fun saveUser(user: ModelUserData?) {
        put(USER_DATA, Gson().toJson(user))
    }

    fun getUser(): ModelUserData? {
        val data = get(USER_DATA, String::class.java)
        if (data.isEmpty()) {
            return null
        }
        return Gson().fromJson(data, ModelUserData::class.java)
    }


    fun saveFirebaseToken(name: String) {
        put(PREF_FIREBASE_TOKEN, name)
    }

    fun getFirebaseToken(): String {
        return get(PREF_FIREBASE_TOKEN, String::class.java)
    }

    fun <T> get(key: String, clazz: Class<T>): T =
        when (clazz) {
            String::class.java -> sharedPref.getString(key, "")
            Boolean::class.java -> sharedPref.getBoolean(key, false)
            Float::class.java -> sharedPref.getFloat(key, -1f)
            Double::class.java -> sharedPref.getFloat(key, -1f)
            Int::class.java -> sharedPref.getInt(key, -1)
            Long::class.java -> sharedPref.getLong(key, -1L)
            else -> null
        } as T

    fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
        }
        editor.apply()
    }

    fun clear() {
        sharedPref.edit().run {
            remove(PREF_TOKEN)
        }.apply()
    }

    fun getPreferredLocale(): String {
        return sharedPref.getString("preferred_locale", "ar")!!
    }

    fun setPreferredLocale(localeCode: String) {
        sharedPref.edit().putString("preferred_locale", localeCode).apply()
    }

}