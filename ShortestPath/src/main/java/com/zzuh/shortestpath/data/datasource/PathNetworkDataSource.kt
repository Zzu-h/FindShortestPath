package com.zzuh.shortestpath.data.datasource

import android.util.Log
import com.zzuh.shortestpath.data.api.FindPathInfo
import com.zzuh.shortestpath.data.repository.BASE_URL
import com.zzuh.shortestpath.data.repository.NetworkState
import com.zzuh.shortestpath.data.repository.Status
import com.zzuh.shortestpath.data.vo.PathResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

internal class PathNetworkDataSource(private val CLIENT_ID: String, private val CLIENT_SECRET: String) {

    fun fetchEachPath(
        start: String,
        end: String,
        option: String = "trafast",
        loadedPathResult: LoadedPathResult? = null
    ){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(FindPathInfo::class.java)

        val callGetPath = api.getPath(
            clientId = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            coords = start,
            output = end,
            code = option
        )
        
        callGetPath.enqueue(object : Callback<PathResult> {
            override fun onFailure(call: Call<PathResult>, t: Throwable) {
                t.printStackTrace()
                loadedPathResult?.apply {
                    networkState = NetworkState(Status.FAILED, t.message.toString())
                    loadError(t.message.toString())
                }
            }

            override fun onResponse(call: Call<PathResult>, response: Response<PathResult>) {
                val result = response.body() as PathResult
                Log.d("fetchEachPath", result.toString())
                loadedPathResult?.apply {
                    networkState = NetworkState(Status.SUCCESS, "Success")
                    loadSuccess(result)
                }
            }
        })
    }

    interface LoadedPathResult {
        var firstIdx: Int
        var secondIdx: Int
        var networkState: NetworkState

        fun loadError(errorMsg: String)
        fun loadSuccess(result: PathResult)
    }
}