package com.zzuh.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.naver.maps.map.overlay.Marker
import com.zzuh.mymap.databinding.ActivityMainBinding

var CLIENT_ID = ""//= R.string.client_id.toString()
var CLIENT_SECRET = ""// R.string.client_secret.toString()
var BASE_URL_NAVER_API = "" //R.string.baseUrl.toString()

var markerData = mutableListOf<Marker>()
var addressData = mutableListOf<String>()

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var transaction: FragmentTransaction
    lateinit var fragmentManager: FragmentManager

    lateinit var mapsFragment: MapsFragment
    lateinit var bottomSheetFragment: BottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CLIENT_ID = getString(R.string.client_id)
        CLIENT_SECRET = getString(R.string.client_secret)
        BASE_URL_NAVER_API = getString(R.string.baseUrl)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapsFragment = MapsFragment()
        bottomSheetFragment = BottomSheetFragment()

        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()

        transaction.add(R.id.maps_fragment, mapsFragment)
        transaction.add(R.id.bottom_sheet_fragment, bottomSheetFragment)
        transaction.commit()
    }
}