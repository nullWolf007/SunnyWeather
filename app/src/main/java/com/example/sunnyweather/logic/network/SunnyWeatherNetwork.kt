package com.example.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {

    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).netRequest()

    //suspend声明是挂起函数
    private suspend fun <T> Call<T>.netRequest(): T {
        //suspendCoroutine可以在挂起函数中使用
        //立即把当前协程挂起，接受一个Lambda表达式，Lambda表达式中的代码会在一个普通的线程中执行
        return suspendCoroutine { continuation ->
            //enqueue进行网络请求 会在一个普通线程中
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("Response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    //让协程恢复执行
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}