package com.amosh.combyne_movieapp.util

fun <T> MutableList<T>.addIfNotExistAndNotNull(item: T?) {
    if (!this.contains(item) && item != null) this.add(item)
}
