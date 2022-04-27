package com.zzuh.shortestpath

import com.naver.maps.geometry.LatLng
import com.zzuh.shortestpath.data.repository.PathRepository
import com.zzuh.shortestpath.model.TSP

class FindShortestPath(private val CLIENT_ID: String, private val CLIENT_SECRET: String): Thread() {

    private val repository = PathRepository(CLIENT_ID, CLIENT_SECRET)
    private val repoListener = object: PathRepository.RequestDataListener{
        override fun onError(errorMsg: String) { requestDataListener?.onError(errorMsg) }
        override fun onSuccess(start: LatLng, end: LatLng) { requestDataListener?.onSuccess(start, end) }

        override fun onAllDataLoaded() {
            requestDataListener?.onAllDataLoaded()
            if(calFlag) startCalculatePath()
        }
    }

    private var tsp: TSP? = null
    private var array = arrayListOf<LatLng>()
    private var calFlag = true

    var requestDataListener: RequestDataListener? = null
    var calculateListener: CalculateListener? = null

    fun fetchPathBoard(array: ArrayList<LatLng>, autoCalculateFlag: Boolean = true, userRequestDataListener: RequestDataListener? = null, userCalculateListener: CalculateListener? = null){
        repository.requestDataListener = repoListener
        calFlag = autoCalculateFlag

        userRequestDataListener?.apply { requestDataListener = userRequestDataListener }
        userCalculateListener?.apply { calculateListener = userCalculateListener }

        this.array = array
        this.start()
    }
    fun startCalculatePath(){
        if(this.array.isEmpty()) {
            calculateListener?.onError("You should call fetchPathBoard first.")
            return
        }

        tsp = TSP(object: TSP.listener{})
            .apply { endCalculate = this@FindShortestPath::endPathCalculate }

        for(startIndex in 0..array.size)
            for(goalIndex in 0..array.size){
                tsp?.setDistArray(startIndex, goalIndex,
                    if (startIndex != goalIndex) repository.pathResultBoard[startIndex][goalIndex]!!.route.summary.distance else 0)
            }

        tsp?.start()
    }

    @Deprecated("for test function")
    fun testCalculate(map: Array<Array<Int>>){
        val size = map.size
        tsp = TSP(object: TSP.listener{})
            .apply {
                endCalculate = this@FindShortestPath::endPathCalculate
                n = size
            }

        for(startIndex in 0 until size)
            for(goalIndex in 0 until size)
                tsp?.setDistArray(startIndex, goalIndex, map[startIndex][goalIndex])

        tsp?.start()
    }

    private fun endPathCalculate(result: List<Int>): Unit?
        = calculateListener?.onEndCalculate(ArrayList<LatLng>()
            .apply {
                result.forEach { i ->
                    this.add(this@FindShortestPath.array[i])
                }
            })
    /*{
        /* array 순서를 재배치 필요 */
        println(array)
        val pathList = ArrayList<LatLng>()
        array.forEach { i -> pathList.add(this.array[i]) }
        calculateListener?.onEndCalculate(ArrayList<LatLng>()
            .apply {
                result.forEach { i ->
                    this.add(this@FindShortestPath.array[i])
                }
            })
    }*/

    override fun run() {
        super.run()
        try{ repository.fetchAllPath(array) }
        catch (e: Exception){ }
    }

    interface RequestDataListener {
        fun onError(errorMsg: String)
        fun onSuccess(start: LatLng, end: LatLng)
        fun onAllDataLoaded()
    }
    interface CalculateListener {
        fun onError(errorMsg: String)
        fun onEndCalculate(path: ArrayList<LatLng>)
    }
}