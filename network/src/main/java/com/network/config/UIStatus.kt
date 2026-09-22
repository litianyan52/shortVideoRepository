package com.network.config

sealed class UIStatus<out T> {
    object Loading : UIStatus<Nothing>()
    data class Success<T>(val data : T) : UIStatus<T>()
    data class Error(val data : String) : UIStatus<Nothing>()
    data class Throwable(val data: String) : UIStatus<Nothing>()
}