package com.example.sunnyweather.logic.model

//返回类
data class PlaceResponse(
    val status: String,
    val query: String,
    val places: List<Place>
)

//地点类
data class Place(
    val id: String,
    val location: Location,
    val pace_id: String,
    val name: String,
    val formatted_address: String
)

//经纬度类
data class Location(val lat: String, val lng: String)