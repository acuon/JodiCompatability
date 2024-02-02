package com.jodi.companioncompatibility.utils.network

sealed class ResultOf<out T> {
    data class Progress(val loading: Boolean = false) : ResultOf<Nothing>()
    data class Success<out R>(val value: R) : ResultOf<R>()
    data class Empty(val message: String? = null) : ResultOf<Nothing>()
    data class Failure(
        val message: String? = null,
        val throwable: Throwable? = null
    ) : ResultOf<Nothing>()
}