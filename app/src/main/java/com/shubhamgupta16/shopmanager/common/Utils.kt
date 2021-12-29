package com.shubhamgupta16.shopmanager.common

import android.app.Activity
import android.content.Context
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat
import java.util.*

val Float.inr: String
    get() {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
        val price = format.format(this)
        return if (price.endsWith(".00")) price.replace(".00", "") else price
    }
val Double.inr: String
    get() {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "in"))
        val price = format.format(this)
        return if (price.endsWith(".00")) price.replace(".00", "") else price
    }

fun View.fadeVisibility(visibility: Int, duration: Long = 400) {
    val transition: Transition = Fade()
    transition.duration = duration
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.visibility = visibility
}
fun Activity.closeKeyboard(){
    currentFocus?.let { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
