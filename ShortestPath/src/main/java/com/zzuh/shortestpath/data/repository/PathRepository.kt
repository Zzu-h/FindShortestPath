package com.zzuh.shortestpath.data.repository

import com.naver.maps.geometry.LatLng
import com.zzuh.shortestpath.data.datasource.PathNetworkDataSource
import com.zzuh.shortestpath.data.vo.PathResult

internal class PathRepository(private val CLIENT_ID: String, private val CLIENT_SECRET: String) {
    val pathNetworkDataSource = PathNetworkDataSource(CLIENT_ID,CLIENT_SECRET)

    var requestDataListener: RequestDataListener? = null
    var pathResultBoard = Array(10) { arrayOfNulls<PathResult>(10) }

    var endCount = 0
    var callCount = 0

    fun fetchAllPath(array: ArrayList<LatLng>, option: String = "trafast"){
        //45
        endCount = array.size * (array.size - 1) / 2

        if(endCount > 45 || endCount <= 0){
            requestDataListener?.onError("Too much/little data set.")
            return
        }

        for(i in 0..array.lastIndex)
            for(k in (i + 1)..array.lastIndex){
                val first = array[i]
                val second = array[k]

                val listener = object: PathNetworkDataSource.LoadedPathResult{
                    override var firstIdx: Int = i
                    override var secondIdx: Int = k
                    override var networkState: NetworkState = NetworkState.LOADED

                    override fun loadError(errorMsg: String) {
                        requestDataListener?.onError(errorMsg)
                        endCheck()
                    }

                    override fun loadSuccess(result: PathResult) {
                        pathResultBoard[i][k] = result
                        pathResultBoard[k][i] = result
                        endCheck()
                        requestDataListener?.onSuccess(first, second)
                    }
                }

                pathNetworkDataSource.fetchEachPath(
                    start = "${first.longitude},${first.latitude}",
                    end = "${second.longitude},${second.latitude}",
                    option = option, listener
                )
            }
    }

    private fun endCheck()
        = (++callCount).apply {
            if(endCount <= callCount)
                requestDataListener?.onAllDataLoaded()
        }


    fun fetchEachPath(
        start: String,
        end: String,
        option: String = "trafast",
    ){
        pathNetworkDataSource.fetchEachPath(start,end,option)
    }

    interface RequestDataListener {
        fun onError(errorMsg: String)
        fun onSuccess(start: LatLng, end: LatLng)
        fun onAllDataLoaded()
    }
}