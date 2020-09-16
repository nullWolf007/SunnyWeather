package com.example.sunnyweather.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Place


class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    //进行内存 缓存
    val placeList = ArrayList<Place>()

    //由于网络接口的数据是从Repository中获取的 所以每次都会创建实例 无法监听
    //所以我们采取了switchMap的方式 去监听
    // 大致流程是 调用searchPlaces方法会把query赋值给searchLiveData 则此livedata对象数据改变了就会执行switchMap中的方法 获取网络数据
    //同时switchMap方法还会将Repository.searchPlaces返回的数据转换成一个可监听的livedata数据类
    val placeLiveData: LiveData<Result<List<Place>>> =
        Transformations.switchMap(searchLiveData) { query ->
            Repository.searchPlaces(query)
        }

    fun searchPlaces(query: String) {
        searchLiveData.value = query

    }
}