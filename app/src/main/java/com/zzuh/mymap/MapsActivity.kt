package com.zzuh.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource

import com.zzuh.mymap.databinding.ActivityMapsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.jar.Manifest

var CLIENT_ID = ""//= R.string.client_id.toString()
var CLIENT_SECRET = ""// R.string.client_secret.toString()
var BASE_URL_NAVER_API = "" //R.string.baseUrl.toString()

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    // Activity UI 관련 변수
    private lateinit var binding: ActivityMapsBinding
    private lateinit var recyclerViewAdapter: MarkerAdapter

    // Naver Map 관련 변수
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapUiSettings: UiSettings
    private lateinit var naverMap: NaverMap


    private var markerData = mutableListOf<Marker>()
    private var addressData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CLIENT_ID = getString(R.string.client_id)
        CLIENT_SECRET = getString(R.string.client_secret)
        BASE_URL_NAVER_API = getString(R.string.baseUrl)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        recyclerViewAdapter = MarkerAdapter(addressData, markerData)
        binding.bottomLayout.recyclerView.adapter = recyclerViewAdapter
        binding.bottomLayout.recyclerView.layoutManager = LinearLayoutManager(this)

        Log.d("tester", "testing2")
        binding.map.getMapAsync(this)
    }

    //@UiThread
    override fun onMapReady(naverMap: NaverMap) {
        mapUiSettings = naverMap.uiSettings
        naverMap.locationSource = locationSource

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        //
        Log.d("tester", "testing")

        mapUiSettings.isLocationButtonEnabled = true

        naverMap.setOnMapClickListener { pointF, latLng ->
            Toast.makeText(this, "${latLng.latitude}, ${latLng.longitude}",Toast.LENGTH_SHORT).show()
            val marker = Marker()
            marker.position = latLng
            marker.setOnClickListener(this)

            getAddressForLanLong(marker, addressData.size)
        }
        this.naverMap = naverMap
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults)){
            if(locationSource.isActivated)
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            else {
                naverMap.locationTrackingMode = LocationTrackingMode.None
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onClick(overlay: Overlay): Boolean {

        if(overlay is Marker){
            overlay.map = null

            // remove Data
            for(i in 1..markerData.size){
                if(overlay.equals(markerData[i-1])){
                    markerData.removeAt(i-1)
                    addressData.removeAt(i-1)
                    break
                }
            }

            recyclerViewAdapter.notifyDataSetChanged()
            return true
        }

        return false
    }

    fun getAddressForLanLong(marker: Marker, index: Int): String{
        val lanlong = "${marker.position.longitude},${marker.position.latitude}"
        //val lanlong = "${marker.position.latitude},${marker.position.longitude}"
        Log.d("tester", lanlong)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NAVER_API)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(AddressInfo::class.java)
        val callGetSearch = api.getAddress(CLIENT_ID, CLIENT_SECRET, lanlong, "json", "roadaddr")
        var result: AddressResult? = null
        callGetSearch.enqueue(object : Callback<AddressResult> {
            override fun onResponse(
                call: Call<AddressResult>,
                response: Response<AddressResult>
            ) {
                //Log.d("결과", "성공 : ${response}")
                result = response.body()
                Log.d("결과", result.toString())
                if(result!!.status.code == 0)
                    addressData.add("${result!!.results.get(0).region.area1.name} ${result!!.results.get(0).region.area2.name} ${result!!.results.get(0).region.area3.name} ${result!!.results.get(0).region.area4.name}")
                else{
                    addressData.add("Invalid Address")
                    Log.d("error",result!!.status.toString())
                }
                marker.map = naverMap
                markerData.add(marker)
                recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<AddressResult>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
                t.printStackTrace()
            }
        })
        return if(result != null){
            addressData.last()
        } else "No Data"
    }
}