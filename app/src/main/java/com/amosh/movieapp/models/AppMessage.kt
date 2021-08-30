package com.amosh.movieapp.models

import com.amosh.movieapp.R

data class AppMessage(
    val id: Int = PRIMARY,
    var message: String = ""
) {
    companion object {
        const val FAILURE = 0
        const val WARNING = 1
        const val SUCCESS = 2
        const val PRIMARY = 3
        const val INFO = 4
    }

    fun getMessageColor(): Int = when (id) {
        FAILURE -> R.color.red_ff3b30
        WARNING -> R.color.yellow_f0ad4e
        SUCCESS -> R.color.green_1ab41e
        PRIMARY, INFO -> R.color.blue_345aa5
        else -> R.color.blue_345aa5
    }
}