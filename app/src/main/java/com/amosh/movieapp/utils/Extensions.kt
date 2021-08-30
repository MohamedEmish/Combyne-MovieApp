package com.amosh.movieapp.utils

import android.view.View
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

fun <T> MutableList<T>.addIfNotExistAndNotNull(item: T?) {
    if (!this.contains(item) && item != null) this.add(item)
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.setIsVisible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

fun EditText.clickableOnly() {
    this.isFocusable = false
    this.isClickable = true
}

fun String.convertFromDateFormatToAnother(currentFormat: String, requiredFormat: String): String {
    val formatter = SimpleDateFormat(requiredFormat, Locale.US)
    val parser = SimpleDateFormat(currentFormat, Locale.US)
    return formatter.format(parser.parse(this) ?: Date())
}


