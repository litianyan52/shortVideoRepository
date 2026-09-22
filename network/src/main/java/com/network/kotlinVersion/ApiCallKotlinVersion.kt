package com.network.kotlinVersion

import com.network.bean.IApiResponse
import com.network.bean.ResBase
import com.network.config.NetworkStatusResult
import kotlin.coroutines.cancellation.CancellationException


object ApiCallKotlinVersion {

    suspend fun <R : IApiResponse , T> enqueue(call: suspend () -> R , successCode : Int = 1 , dataType : (R) -> T?): NetworkStatusResult<T> {
        return try {
            val response = call()
            if (response.getCode() == successCode) {
                val data = dataType(response)
                when {
                    data != null -> NetworkStatusResult.Success(data)
                    else -> NetworkStatusResult.Empty(response.getMsg())
                }

            } else {
                NetworkStatusResult.Error(response.getMsg())
            }
        } catch (e: CancellationException) {
            throw e                       // 必须原样抛出，让取消正常传播
        } catch (e: Exception) {
            NetworkStatusResult.Error("网络异常，连请求都没成功")
        }
    }




}