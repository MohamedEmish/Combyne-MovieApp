package com.amosh.movieapp.ui.addMoview

import org.junit.Test

class AddMovieUtilTest {

    @Test
    fun `emptyTitleDataReturnsFalse`() {
        val result = AddMovieUtil.validateNewMovieInput(
            title = "",
            seasons = 1,
            date = "1 May 2021"
        )
        assert(!result)
    }

    @Test
    fun `emptyDateDataReturnsFalse`() {
        val result = AddMovieUtil.validateNewMovieInput(
            title = "title",
            seasons = 1,
            date = ""
        )
        assert(!result)
    }

    @Test
    fun `emptySeasonOrZeroDataReturnsFalse`() {
        val result = AddMovieUtil.validateNewMovieInput(
            title = "title",
            seasons = 0,
            date = "1 May 2021"
        )
        assert(!result)
    }

    @Test
    fun `allDataFilledReturnsTrue`() {
        val result = AddMovieUtil.validateNewMovieInput(
            title = "title",
            seasons = 2,
            date = "1 May 2021"
        )
        assert(result)
    }

}