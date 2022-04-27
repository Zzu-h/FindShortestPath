package com.zzuh.shortestpath.data.vo

internal data class Route(
    var summary: Summary,
    var path: List<List<Double>>,
    var section: List<Section>,
    var guide: List<Guide>
)