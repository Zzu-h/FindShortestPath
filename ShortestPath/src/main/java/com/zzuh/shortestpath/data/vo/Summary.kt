package com.zzuh.shortestpath.data.vo

internal data class Summary(
    var start: Start,
    var goal: Goal,
    var distance: Int,
    var duration: Int,
    var bbox: List<List<Float>>,
    var tollFare: Int,
    var taxiFare: Int,
    var fuelPrice: Int,
)