package com.zzuh.mymap

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zzuh.mymap.databinding.ResultRecyclerviewBinding

class PathResultViewHolder(val binding: ResultRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class PathResultAdapter(val instruction: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return instruction.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = PathResultViewHolder(ResultRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as PathResultViewHolder).binding

        Log.d("tester", BASE_URL_NAVER_API as String)

        binding.itemData.text = instruction[position]
        binding.itemIndex.text = "${position+1}"
    }
}
