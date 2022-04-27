package com.zzuh.shortestpath.data.vo

internal data class Guide(
    var pointIndex: Int,
    var type: Int,
    var instructions: String,
    var distance: Int,
    var duration: Int
)