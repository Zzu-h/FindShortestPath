package com.zzuh.mymap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zzuh.mymap.databinding.FragmentSelectedListBinding

class SelectedListFragment : Fragment() {

    lateinit var binding: FragmentSelectedListBinding
    lateinit var adapter: MarkerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity

        binding = FragmentSelectedListBinding.inflate(layoutInflater)
        adapter = MarkerAdapter(addressData, markerData)
        binding.selectedListRecyclerView.adapter = adapter

        (activity as MainActivity).notifyCallback = {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}