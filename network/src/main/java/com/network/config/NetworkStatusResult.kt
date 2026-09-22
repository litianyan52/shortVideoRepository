package com.network.config

sealed interface NetworkStatusResult<out T> {
    data class Success<T>(val data: T): NetworkStatusResult<T>
    data class Empty(val msg:String?=null): NetworkStatusResult<Nothing>
    data class Error(val errorMessage:String?): NetworkStatusResult<Nothing>
}

//sealed interface NetworkResult<out T> {
//    data class Success<T>(val data: T) : NetworkResult<T>
//    data class Empty(val msg: String? = null) : NetworkResult<Nothing>
//    data class BusinessError(val code: Int, val message: String?) : NetworkResult<Nothing>
//    data class NetworkError(val throwable: Throwable) : NetworkResult<Nothing>
//}