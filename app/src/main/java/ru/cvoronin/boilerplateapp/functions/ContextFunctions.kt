package ru.simpls.brs2.commons.functions

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import org.jetbrains.anko.AnkoContext

fun AnkoContext<*>.dial(number: String): Boolean = ctx.dial(number)
fun Fragment.dial(number: String): Boolean = activity.dial(number)

fun Context.dial(number: String): Boolean {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

fun Context.isGranted(permission : String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED