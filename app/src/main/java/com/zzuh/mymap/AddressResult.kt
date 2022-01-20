package com.zzuh.mymap


data class AddressResult (
    var status: Status,
    var results: List<Result>
)
data class Status(
    var code: Int,
    var name: String,
    var message: String
)

data class Result(
    var name: String,
    var code: Code,
    var region: Region,
    var land: Land
)

data class Code(
    var id: String,
    var type: String,
    var mappingId: String
)
data class Region(
    var area0: Area,
    var area1: Area,
    var area2: Area,
    var area3: Area,
    var area4: Area

)
data class Area(
    var name: String,
    var coords: Coords
)
data class Land(
    var type: String,
    var number1: String,
    var number2: String,
    var name: String,
    var addition0: Addition,
    var addition1: Addition,
    var addition2: Addition,
    var addition3: Addition,
    var addition4: Addition,
    var coords: Coords
)
data class Addition(
    var type: String,
    var value: String
)
data class Coords(
    var center: Center
)
data class Center(
    var crs: String,
    var x: Float,
    var y: Float
)