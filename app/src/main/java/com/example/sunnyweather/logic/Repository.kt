package com.example.sunnyweather.logic

import android.content.Context
import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.RealtimeResponse
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    /**
     * 获取地点
     */
    //网络请求要运行在IO线程中，不能运行在主线程中
    //同时liveData函数 有默认参数
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        //调用网络接口 返回Call<T>对象
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        //对数据进行判断处理
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("Response status is ${placeResponse.status}"))
        }
    }

    /**
     * 获取天气
     */
    //网络请求要运行在IO线程中，不能运行在主线程中
    //同时liveData函数 有默认参数
    fun refreshMeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            //使用async协程
            val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
            val deferredDaily = async { SunnyWeatherNetwork.getDailyWeather(lng, lat) }
            //最后再进行wait达到同时进行的效果 不然会阻塞协程
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            //对数据进行处理
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtimeResponse status is $realtimeResponse.status," +
                                "dailyResponse status is $dailyResponse.status,"
                    )
                )
            }
        }
    }

    /**
     * 使用高级函数  用来统一处理外部的异常
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            //类似livedata的setValue方法，只不过没有livedata对象，所以只能用emit方法
            emit(result)
        }

    /**
     * place shared相关
     */
    //保存
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    //获取
    fun getSavedPlace() = PlaceDao.getSavedPlace()

    //判断是否存在
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}