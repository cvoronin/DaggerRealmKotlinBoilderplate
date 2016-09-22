package ru.cvoronin.boilerplateapp.functions

import android.app.Activity
import android.support.v4.util.Pair
import android.view.View
import org.jetbrains.anko.inputMethodManager
import java.util.*

@Suppress("unused")
fun sharedElementsToArray(map: Map<View, String>): Array<android.support.v4.util.Pair<View, String>> {
    val pairList = ArrayList<Pair<View, String>>()

    for ((k, v) in map) {
        val pair = android.support.v4.util.Pair<View, String>(k, v)
        pairList.add(pair)
    }

    return pairList.toTypedArray()
}

@Suppress("unused")
fun emptySharedElements(): Array<android.support.v4.util.Pair<View, String>> {
    val pairList = ArrayList<Pair<View, String>>()
    return pairList.toTypedArray()
}

@Suppress("unused")
fun fadeOut(view: View, delay: Long = 0, endVisibility: Int = View.GONE, duration: Long? = null) {
    if (view.visibility == View.VISIBLE) {
        val animationDuration = duration ?: 1L * view.resources.getInteger(android.R.integer.config_shortAnimTime)
        view.clearAnimation()
        view.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setStartDelay(delay)
                .withEndAction { view.visibility = endVisibility }
                .start()
    }
}

@Suppress("unused")
fun fadeIn(view: View, delay: Long = 0, duration: Long? = null) {
    if (view.visibility != View.VISIBLE) {
        val animationDuration = duration ?: 1L * view.resources.getInteger(android.R.integer.config_mediumAnimTime)

        view.clearAnimation()
        view.alpha = 0f
        view.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setStartDelay(delay)
                .withStartAction { view.visibility = View.VISIBLE }
                .start()
    } else {
        view.alpha = 1f
    }
}


@Suppress("unused")
fun hideKeyboard(view: View?) {
    if (view != null) {
        view.context.inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus)
}

fun View.show() {
    alpha = 1.0f
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.fadeIn() {
    fadeIn(this)
}

fun View.fadeOut() {
    fadeOut(this)
}
