package ru.simpls.brs2.commons.functions

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.connectivityManager
import timber.log.Timber
import java.lang.reflect.Type
import java.net.URLEncoder

fun checkMinSdk(minSdk: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT >= minSdk) {
        f()
    }
}

fun checkMinSdkElse(minSdk: Int, f: () -> Unit) {
    if (Build.VERSION.SDK_INT < minSdk) f()
}

fun String.urlEncode() = URLEncoder.encode(this, "UTF-8")

fun Context.isOnline() = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false

fun now() = System.currentTimeMillis()

inline fun safe(f: (() -> Unit)) {
    try {
        f()
    } catch (e: Exception) {
        Timber.e(e, e.message)
    }
}

inline fun wasInit(f: () -> Unit): Boolean {
    try {
        f()
    } catch(e: UninitializedPropertyAccessException) {
        return false
    }
    return true
}

fun Any.instanceName() : String ="${javaClass.name} @${Integer.toHexString(hashCode())}"
