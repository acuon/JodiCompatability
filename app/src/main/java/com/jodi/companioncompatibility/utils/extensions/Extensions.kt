package com.jodi.companioncompatibility.utils.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.text.InputFilter
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.jodi.companioncompatibility.JodiApp
import com.jodi.companioncompatibility.R
import kotlin.math.roundToInt

fun Activity.hideSoftKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun TextInputEditText.setFocusListener(bottomBorder: View, showHint: Boolean = true) {
    val focused = ContextCompat.getColor(context, R.color.app_color)
    val default = ContextCompat.getColor(context, R.color.grey)
    setOnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            if (showHint) setHintTextColor(focused)
            bottomBorder.setBackgroundColor(focused)
        } else {
            if (showHint) setHintTextColor(default)
            bottomBorder.setBackgroundColor(default)
        }
    }
}

fun getStringFromResource(id: Int): String {
    return JodiApp.getAppContext().getString(id)
}

fun ViewGroup.inflater() = LayoutInflater.from(context)


fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun TextInputEditText.setMaxLength(maxLength: Int) {
    val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    this.filters = filters
}

fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return view
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = view
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback
}


fun Float.toDp(displayMetrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

fun ViewGroup.LayoutParams.setMatchParent() {
    width = ViewGroup.LayoutParams.MATCH_PARENT
}

fun ViewGroup.LayoutParams.setGone() {
    width = 0
}

fun Activity.setDialogMetrics(dialog: Dialog, maxWidth: Float = 1f, maxHeight: Float = 1f) {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(dialog.window!!.attributes)

    layoutParams.width = (displayMetrics.widthPixels * maxWidth).toInt()
    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
    dialog.window!!.attributes = layoutParams
}


fun Activity.getColorRes(@ColorRes id: Int) = ContextCompat.getColor(applicationContext, id)

fun Context.getDrawableRes(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.getMyColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty()