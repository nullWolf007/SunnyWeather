package com.example.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Location

class WeatherViewModel : ViewModel() {
    //监听数据变化
    private val locationLiveData = MutableLiveData<Location>()

    //界面数据
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    //使用switchmao监听数据变化
    val weatherLiveData = Transformations.switchMap(locationLiveData) {
        Repository.refreshMeather(it.lng, it.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lat, lng)
    }
}