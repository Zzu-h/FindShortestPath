package com.zzuh.mymap

import android.content.Context
import android.graphics.Color
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.naver.maps.geometry.LatLng

import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.zzuh.mymap.databinding.FragmentMapsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MapsFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener, Handler.Callback {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    // Fragment UI
    private lateinit var selectedListAdapter: MarkerAdapter
    private lateinit var binding: FragmentMapsBinding
    private lateinit var mainActivity: MainActivity

    // Naver Map 관련 변수
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapUiSettings: UiSettings
    private lateinit var naverMap: NaverMap
    private lateinit var location: Location
    private var latitude = 0.0
    private var longitude = 0.0
    private val pathOverlay = PathOverlay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMapsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        binding.map.getMapAsync(this)
        return binding.root
    }

    override fun handleMessage(p0: Message): Boolean {
        TODO("Not yet implemented")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onMapReady(naverMap: NaverMap) {
        mapUiSettings = naverMap.uiSettings
        naverMap.locationSource = locationSource

        naverMap.addOnLocationChangeListener{
            location -> {
                latitude = location.latitude
                longitude = location.longitude
        }
        }

        ActivityCompat.requestPermissions(
            mainActivity,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
        mapUiSettings.isLocationButtonEnabled = true

        naverMap.setOnMapClickListener { pointF, latLng ->
            Toast.makeText(mainActivity, "${latLng.latitude}, ${latLng.longitude}", Toast.LENGTH_SHORT).show()
            val marker = Marker()
            marker.position = latLng
            marker.setOnClickListener(this)

            getAddressForLanLong(marker, 0)//addressData.size)
        }
        this.naverMap = naverMap
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
            mainActivity.notifyCallback()
            // selectedListAdapter.notifyDataSetChanged()
            return true
        }
        return false
    }

    fun getCurrentLanLong(){
        var marker = Marker()
        marker.position = LatLng(latitude,longitude)
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
        getAddressForLanLong(marker, 0)
    }
    fun getResultPath(){
        var list = mutableListOf<LatLng>()
        list.add(LatLng(latitude,longitude))

        for(pathItem in resultData){
            for(item in pathItem.route.path)
                list.add(LatLng(item.get(1),item.get(0)))
        }

        pathOverlay.coords = list
        pathOverlay.map = naverMap
    }

    fun getAddressForLanLong(marker: Marker, index: Int){
        val lanlong = "${marker.position.longitude},${marker.position.latitude}"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NAVER_API)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(AddresInfo::class.java)
        val callGetSearch = api.getAddress(CLIENT_ID, CLIENT_SECRET, lanlong, "json", "roadaddr")
        var result: AddressResult? = null
        callGetSearch.enqueue(object : Callback<AddressResult> {
            override fun onResponse(
                call: Call<AddressResult>,
                response: Response<AddressResult>
            ) {
                result = response.body()
                if(result!!.status.code == 0)
                    addressData.add("${result!!.results.get(0).region.area1.name} ${result!!.results.get(0).region.area2.name} ${result!!.results.get(0).region.area3.name} ${result!!.results.get(0).region.area4.name}")
                else{
                    addressData.add("Invalid Address")
                    Log.d("error",result!!.status.toString())
                }
                marker.map = naverMap
                markerData.add(marker)
                Log.d("Tester", "send notify")
                mainActivity.notifyCallback()
            }

            override fun onFailure(call: Call<AddressResult>, t: Throwable) {
                Log.d("결과:", "실패 : $t")
                t.printStackTrace()
            }
        })
    }

    fun uiClear(){
        for(marker in markerData)
            marker.map = null
        markerData.clear()
        pathOverlay.coords.clear()
        pathOverlay.map = null
    }
}