package com.zzuh.shortestpath.data.repository

const val BASE_URL = "https://naveropenapi.apigw.ntruss.com"

internal enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

internal class NetworkState(val status: Status, val msg: String) {
    companion object {

        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAILED, "Something went wrong")
        }
    }
}