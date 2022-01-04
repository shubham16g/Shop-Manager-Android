package com.shubhamgupta16.shopmanager.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.text.InputType
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

val Int.dp get() = this / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
val Float.dp get() = this / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

val Int.dpi get() = this.dp.roundToInt()

val Int.px get() = this * Resources.getSystem().displayMetrics.density
val Float.px get() = this * Resources.getSystem().displayMetrics.density

val Number.valueString: String
    get() {
        val price = this.toString()
        return if (price.endsWith(".0")) price.replace(".0", "") else price
    }
val Number.inr: String
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

fun Activity.closeKeyboard() {
    currentFocus?.let { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.alertDialogBuilder(
    title: String,
    message: String,
    positiveBtn: String,
    positiveBtnListener: ((dialog: DialogInterface, i: Int) -> Unit)?,
    negativeBtn: String = "CANCEL",
    negativeBtnListener: ((dialog: DialogInterface, i: Int) -> Unit)? = null
): AlertDialog.Builder {
    return AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveBtn, positiveBtnListener)
        .setNegativeButton(negativeBtn, negativeBtnListener)
}