package com.zzuh.mymap

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.naver.maps.map.overlay.Marker
import com.zzuh.mymap.MessageCode.CAL_PATHS
import com.zzuh.mymap.MessageCode.CAL_SERVICE_DONE
import com.zzuh.mymap.MessageCode.CONNECT_OK
import com.zzuh.mymap.MessageCode.GET_PATHS
import com.zzuh.mymap.MessageCode.GET_SERVICE_DONE
import com.zzuh.mymap.MessageCode.READY_SERVICE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class GetPathService : Service() {

    lateinit var messenger: Messenger
    lateinit var replyMessenger: Messenger

    lateinit var retrofit: Retrofit
    lateinit var api: FindPathInfo

    inner class InCommingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                CONNECT_OK -> {
                    // connection 되자마자 시작되는 메소드 / 초기 설정
                    replyMessenger = msg.replyTo
                    Log.d("tester Service", "Receive code CONNECT_OK")

                    val replyMsg = Message()
                    replyMsg.what = READY_SERVICE

                    replyMessenger.send(replyMsg)
                }
                GET_PATHS -> {
                    Log.d("tester Service", "Receive code GET_PATHS")
                    requestPaths()
                    val replyMsg = Message()
                    replyMsg.what = GET_SERVICE_DONE

                    replyMessenger.send(replyMsg)
                }
                CAL_PATHS -> {
                    Log.d("tester Service", "Receive code CAL_PATHS")
                    calculatePaths()
                    val replyMsg = Message()
                    replyMsg.what = CAL_SERVICE_DONE

                    replyMessenger.send(replyMsg)
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate() {
        messenger = Messenger(InCommingHandler(this))
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NAVER_API)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(FindPathInfo::class.java)
        super.onCreate()
    }
    override fun onBind(intent: Intent): IBinder = messenger.binder

    private fun requestPaths(){ }
    private fun calculatePaths(){ }

    fun getPaths(markerStart: Marker, markerGoal: Marker){
        val callGetSearch = api.getPath(
            CLIENT_ID, CLIENT_SECRET,
            "${markerStart.position.longitude},${markerStart.position.latitude}",
            "${markerGoal.position.longitude},${markerGoal.position.latitude}",
            "trafast"
        )
        var result: PathResult? = null
        callGetSearch.enqueue(object : Callback<PathResult> {
            override fun onFailure(call: Call<PathResult>, t: Throwable) {
                TODO("Not yet implemented")
                Log.d("결과:", "실패 : $t")
            }

            override fun onResponse(call: Call<PathResult>, response: Response<PathResult>) {
                TODO("Not yet implemented")
                result = response.body()
            }
        })
    }
}