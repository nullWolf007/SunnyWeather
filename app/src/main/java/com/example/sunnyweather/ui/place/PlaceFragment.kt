package com.example.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.MainActivity

import com.example.sunnyweather.R
import com.example.sunnyweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {
    val placeViewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var placeAdapter: PlaceAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //从shared中获取数据
        if (activity is MainActivity && placeViewModel.isPlaceSaved()) {
            val place = placeViewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return

        }

        //recyclerview设置
        val linearLayoutManager = LinearLayoutManager(context)
        fragment_place_recyclerView.layoutManager = linearLayoutManager
        placeAdapter = PlaceAdapter(this, placeViewModel.placeList)
        fragment_place_recyclerView.adapter = placeAdapter

        //监听搜索框
        fragment_place_et_search.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                placeViewModel.searchPlaces(content)
            } else {
                fragment_place_recyclerView.visibility = View.GONE
                fragment_place_iv_bg.visibility = View.GONE
                placeViewModel.placeList.clear()
                placeAdapter.notifyDataSetChanged()
            }
        }

        //观察数据变化
        placeViewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                //如果数据不为空 则刷新recyclerview
                fragment_place_recyclerView.visibility = View.VISIBLE
                fragment_place_iv_bg.visibility = View.VISIBLE
                placeViewModel.placeList.clear()
                placeViewModel.placeList.addAll(places)
                placeAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })


    }

}
