package com.zzuh.mymap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AddresInfo {
    @GET("map-reversegeocode/v2/gc")
    fun getAddress(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("coords") coords: String,
        @Query("output") output: String,
        @Query("orders") code: String,
    ): Call<AddressResult>
}

interface FindPathInfo {
    @GET("map-direction/v1/driving")
    fun getPath(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("start") coords: String,
        @Query("goal") output: String,
        @Query("option") code: String,
    ): Call<AddressResult>
}