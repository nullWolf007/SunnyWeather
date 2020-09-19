package com.example.sunnyweather.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.sunnyweather.BaseApplication
import com.example.sunnyweather.logic.model.Place
import com.google.gson.Gson

object PlaceDao {
    const val SHARED_KEY_PLACE = "place"

    val gson = Gson()
    var sharedPreferences: SharedPreferences? = null

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString(SHARED_KEY_PLACE, gson.toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString(SHARED_KEY_PLACE, "")
        return gson.fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains(SHARED_KEY_PLACE)

    private fun sharedPreferences(): SharedPreferences {
        if (sharedPreferences == null) {
            sharedPreferences =
                BaseApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)
        }
        return sharedPreferences!!
    }
}