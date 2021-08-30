package com.amosh.movieapp.models

import com.amosh.combyne_movieapp.fragment.PageInfo

sealed class AppResponseState<out R> {
    data class Success<out T>(val data: T?, val pageInfo: PageInfo? = null) : AppResponseState<T>()
    data class Error(val message: String) : AppResponseState<Nothing>()
    object Loading : AppResponseState<Nothing>()
}