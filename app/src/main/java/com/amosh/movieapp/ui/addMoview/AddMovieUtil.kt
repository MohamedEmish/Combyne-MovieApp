package com.amosh.movieapp.ui.addMoview

object AddMovieUtil {

    /**
     * the input is not valid if ...
     * ... any data input is empty
     */
    fun validateNewMovieInput(
        title: String,
        date: String,
        seasons: Int
    ): Boolean {
        return !(title.isEmpty()
                || date.isEmpty()
                || seasons < 1)
    }
}