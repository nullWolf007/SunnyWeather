package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    //网络请求要运行在IO线程中，不能运行在主线程中
    //同时liveData函数 有默认参数
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            //调用网络接口 返回Call<T>对象
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            //对数据进行判断处理
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("Response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        //类似livedata的setValue方法，只不过没有livedata对象，所以只能用emit方法
        emit(result)
    }
}