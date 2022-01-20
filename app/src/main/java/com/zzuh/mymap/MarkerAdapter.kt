package com.zzuh.mymap

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naver.maps.map.overlay.Marker
import com.zzuh.mymap.databinding.ItemRecyclerviewBinding

class MarkerViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MarkerAdapter(val address: MutableList<String>,val marker: MutableList<Marker>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return address.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = MarkerViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MarkerViewHolder).binding

        Log.d("tester", BASE_URL_NAVER_API as String)

        binding.itemData.text = address[position]
        binding.itemIndex.text = "${position+1}"
        binding.itemRoot.setOnClickListener {
            address.removeAt(position)
            marker.get(position).map = null
            marker.removeAt(position)
            super.notifyDataSetChanged()
        }
    }
}
