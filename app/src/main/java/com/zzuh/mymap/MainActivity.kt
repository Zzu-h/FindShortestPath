package com.zzuh.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.naver.maps.map.overlay.Marker
import com.zzuh.mymap.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

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

    var notifyCallback: () -> Unit = {}
    var searchButtonClick: Boolean = true

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

        binding.searchButton.setOnClickListener {
            Log.d("Tester", "click the Search button")
            if(searchButtonClick) {
                //getPaths()
                binding.searchButton.text = "초기화"
                searchButtonClick = false
            }
            else{
                // Data 초기화
                addressData.clear()
                for(marker in markerData)
                    marker.map = null
                markerData.clear()
                notifyCallback()
                binding.searchButton.text = "검색"
                searchButtonClick = true
            }
        }
    }
    fun getPaths(markerStart:Marker,markerGoal:Marker){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NAVER_API)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(FindPathInfo::class.java)
        val callGetSearch = api.getPath(
            CLIENT_ID, CLIENT_SECRET,
            "${markerStart.position.longitude},${markerStart.position.latitude}",
            "${markerGoal.position.longitude},${markerGoal.position.latitude}",
            "trafast"
        )
        var result: PathResult? = null
        callGetSearch.enqueue(object : Callback<PathResult> {
            override fun onResponse(
                call: Call<PathResult>,
                response: Response<PathResult>
            ) {
                result = response.body()
            }

            override fun onFailure(call: Call<PathResult>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
                t.printStackTrace()
            }
        })
    }
}