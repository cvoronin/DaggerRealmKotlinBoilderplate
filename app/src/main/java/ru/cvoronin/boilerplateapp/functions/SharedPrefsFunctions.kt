package ru.cvoronin.boilerplateapp.functions

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

inline fun <reified T> SharedPreferences.save(obj: T, key: String, gson: Gson) =
        edit().putString(key, gson.toJson(obj)).apply()

inline fun <reified T> SharedPreferences.load(key: String, gson: Gson): T? {
    val jsonStr = getString(key, null) ?: return null

    try {
        return gson.fromJson(jsonStr, gsonType<T>())
    } catch (e: Exception) {
        Timber.e(e, "Failed to load instance of ${T::class}, key: $key")
        return null
    }
}

fun SharedPreferences.clear(key: String) = edit().remove(key).apply()

inline fun <reified T> gsonType() = object : TypeToken<T>() {}.type


