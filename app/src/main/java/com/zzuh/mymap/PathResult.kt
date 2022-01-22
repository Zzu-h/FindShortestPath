package com.zzuh.mymap
import java.util.*

data class PathResult (
    var status: Status,
    var message: String,
    var currentDateTime: Date,
    var route: Route
)
data class Route(
    var summary: Summary,
    var path: Path,
    var section: List<Section>,
    var guide: Guide
)

data class Summary(
    var start: Start,
    var goal: Goal,
    var distance: Int,
    var duration: Int,
    var bbox: List<List<Float>>,
    var tollFare: Int,
    var taxiFare: Int,
    var fuelPrice: Int,
)
data class Start(
    var location: List<Float>
)
data class Goal(
    var location: List<Float>,
    var dir:Int
)
data class Path(
    var id: String,
    var type: String,
    var mappingId: String
)
data class Section(
    var pointIndex: Int,
    var pointCount: Int,
    var distance: Int,
    var name: String,
    var congestion: Int,
    var speed: Int
)
data class Guide(
    var pointIndex: Int,
    var type: Int,
    var instructions: String,
    var distance: Int,
    var duration: Int
)