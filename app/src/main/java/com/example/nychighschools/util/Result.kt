package com.example.nychighschools.util

sealed class Result<out R> {
    object Loading: Result<Nothing>()
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val e: Throwable): Result<Nothing>()
}