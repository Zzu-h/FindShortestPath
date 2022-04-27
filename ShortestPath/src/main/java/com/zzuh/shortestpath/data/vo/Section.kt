package com.zzuh.shortestpath.data.vo

internal data class Section(
    var pointIndex: Int,
    var pointCount: Int,
    var distance: Int,
    var name: String,
    var congestion: Int,
    var speed: Int
)
