package com.zzuh.mymap

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.zzuh.mymap.MessageCode.CAL_PATHS
import com.zzuh.mymap.MessageCode.CAL_SERVICE_DONE
import com.zzuh.mymap.MessageCode.CONNECT_OK
import com.zzuh.mymap.MessageCode.ERROR_CODE
import com.zzuh.mymap.MessageCode.GET_PATHS
import com.zzuh.mymap.MessageCode.GET_SERVICE_DONE
import com.zzuh.mymap.MessageCode.READY_SERVICE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class FindShortestPath{
    var W = Array(10, {Array<Int>(10, { -1 })})
    var n = 0

    val visited = BooleanArray(16){false}
    val city = IntArray(16){ -1 }
    var minCost = 987654321
    val minCity = IntArray(16){ -1 }

    fun setDistArray(y: Int, x: Int, value: Int) { W[y][x] = value }

    fun getPaths(size: Int): IntArray {
        n = size
        tsp(n-1,0,1)
        return city
    }

    private fun tsp(node: Int, costSum: Int, count: Int){
        visited[node] = true
        city[count-1] = node
        if(count == n){
            if(costSum < minCost) {
                for (i in 0..(n - 1)) {
                    minCity[i] = city[i]
                }
                minCost = costSum
            }
            visited[node] = false
            city[count-1] = -1
            return
        }
        for(i in 0..(n-1)){
            if(!visited[i] && W[node][i] != 0)
                tsp(i, costSum+W[node][i], count+1)
        }
        visited[node] = false
        city[count-1] = -1
    }
}

class GetPathService : Service() {

    lateinit var messenger: Messenger
    lateinit var replyMessenger: Messenger

    lateinit var retrofit: Retrofit
    lateinit var api: FindPathInfo

    var pathResultSets = Array(10, {Array<PathResult?>(10, { null })})
    var shortestNodePaths = IntArray(16)
    var endCount = 0
    var dataSize = 0

    var findPaths: FindShortestPath = FindShortestPath()

    inner class InCommingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                CONNECT_OK -> {
                    // connection ???????????? ???????????? ????????? / ?????? ??????
                    replyMessenger = msg.replyTo
                    Log.d("tester Service", "Receive code CONNECT_OK")
                    dataSize = markerData.size - 1
                    endCount = dataSize * dataSize
                    val replyMsg = Message()
                    replyMsg.what = READY_SERVICE

                    replyMessenger.send(replyMsg)
                }
                GET_PATHS -> {
                    Log.d("tester Service", "Receive code GET_PATHS")
                    requestPaths()
                    while(endCount > 0){ null }
                    val replyMsg = Message()

                    replyMsg.what =
                    if(endCount < 0) ERROR_CODE
                    else GET_SERVICE_DONE

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
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_NAVER_API)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(FindPathInfo::class.java)
        super.onCreate()
    }
    override fun onBind(intent: Intent): IBinder {
        messenger = Messenger(InCommingHandler(this))
        return messenger.binder
    }

    private fun requestPaths(){
        for(startIndex in 0..dataSize)
            for(goalIndex in 0..dataSize){
                if (endCount < 0) return
                else if (startIndex == goalIndex) continue
                else getPaths(startIndex, goalIndex)
            }
    }
    private fun calculatePaths(){
        for(startIndex in 0..dataSize)
            for(goalIndex in 0..dataSize){
                findPaths.setDistArray(startIndex,goalIndex,if (startIndex != goalIndex) pathResultSets[startIndex][goalIndex]!!.route.summary.distance else 0)
            }
        shortestNodePaths = findPaths.getPaths(dataSize+1)
        for(item in shortestNodePaths)
            Log.d("tester","$item")
        /*for(i in 0..(shortestNodePaths.size-2))
            resultData.add(
                pathResultSets[
                        shortestNodePaths[i]
                ][
                        shortestNodePaths[i+1]
                ]!!
            )*/
    }

    fun getPaths(startIndex: Int, goalIndex: Int){
        var markerStart = markerData[startIndex]
        var markerGoal = markerData[goalIndex]
        val callGetSearch = api.getPath(
            CLIENT_ID, CLIENT_SECRET,
            "${markerStart.position.longitude},${markerStart.position.latitude}",
            "${markerGoal.position.longitude},${markerGoal.position.latitude}",
            "trafast"
        )
        var result: PathResult? = null
        callGetSearch.enqueue(object : Callback<PathResult> {
            override fun onFailure(call: Call<PathResult>, t: Throwable) {
                Log.d("??????:", "?????? : $t")
                endCount = -1
            }

            override fun onResponse(call: Call<PathResult>, response: Response<PathResult>) {
                result = response.body() as PathResult
                Log.d("??????:", "?????? : ${result!!.message} $endCount")
                pathResultSets[startIndex][goalIndex] = result!!
                endCount -= 1
            }
        })
    }
}