package com.example.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*

class PlaceAdapter(private val fragment: PlaceFragment, val datas: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)

        //item项点击事件 跳转
        val holder = ViewHolder(view)
        val activity = fragment.activity
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val place = datas[position]
            if (activity is WeatherActivity) {//关闭侧滑栏
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            } else {//跳转
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                activity?.finish()
            }
            fragment.placeViewModel.savePlace(place)

        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = datas[position]
        holder.place_item_place_name.text = place.name
        holder.place_item_place_address.text = place.formatted_address
    }

    override fun getItemCount() = datas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val place_item_place_name: TextView = view.findViewById(R.id.place_item_place_name)
        val place_item_place_address: TextView = view.findViewById(R.id.place_item_place_address)
    }
}