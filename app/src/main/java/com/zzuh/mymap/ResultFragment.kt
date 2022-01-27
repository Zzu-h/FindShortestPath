package com.zzuh.mymap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zzuh.mymap.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    lateinit var binding: FragmentResultBinding
    lateinit var adapter: PathResultAdapter

    var guideList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentResultBinding.inflate(layoutInflater)
        adapter = PathResultAdapter(guideList)
        binding.resultPathRecyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    fun getResultPath(){
        var index = 1
        for(pathItem in resultData){
            for(item in pathItem.route.guide)
                guideList.add(item.instructions)
            guideList.add("---$index 번째 위치 도착---")
            index++
        }
        adapter.notifyDataSetChanged()
    }
}