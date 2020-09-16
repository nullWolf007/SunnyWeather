package com.example.sunnyweather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Place

class PlaceAdapter(val data: List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = data[position]
        holder.place_item_place_name.text = place.name
        holder.place_item_place_address.text = place.formatted_address
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val place_item_place_name: TextView = view.findViewById(R.id.place_item_place_name)
        val place_item_place_address: TextView = view.findViewById(R.id.place_item_place_address)
    }
}