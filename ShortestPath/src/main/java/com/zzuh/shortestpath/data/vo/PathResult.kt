package com.zzuh.shortestpath.data.vo

import java.util.*

internal data class PathResult (
    var status: Status,
    var message: String,
    var currentDateTime: Date,
    var route: Route
)