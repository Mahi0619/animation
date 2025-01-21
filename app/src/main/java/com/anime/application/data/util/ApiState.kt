package com.anime.application.data.util



sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    object Empty : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Failure(val error: Throwable) : ApiState<Nothing>()
}