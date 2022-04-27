package com.zzuh.shortestpath


class TestClass {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val findShortestPath = FindShortestPath("","")
            findShortestPath.testCalculate(arrayOf(
                arrayOf(0, 10, 15, 20),
                arrayOf(5, 0, 9, 10),
                arrayOf(6, 13, 0, 12),
                arrayOf(8, 8, 9, 0),
            ))
            // 0 1 3 2 0
        }
    }
}