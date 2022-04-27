package com.zzuh.shortestpath.model

import java.lang.Exception
import java.util.*
import kotlin.math.min
/// https://www.koreascience.or.kr/article/JAKO200311922299088.pdf
internal class TSP(listener: listener): Thread(){
    var endCalculate: (List<Int>) -> Unit? = {}
    private var W = Array(10) { Array<Int>(10) { -1 } }

    private var cache = Array(17) { Array<Int>(65536) { 0 } }
    private var dist = Array(17) { Array<Int>(17) { 0 } }

    private val MAX_VALUE = 987654321
    var path = mutableListOf<Int>()

    var n = 0

    private val visited = BooleanArray(16){false}
    private val city = IntArray(16){ -1 }
    private var minCost = 987654321
    private val minCity = IntArray(16){ -1 }

    fun setDistArray(y: Int, x: Int, value: Int) { dist[y][x] = value }

    override fun run() {
        super.run()
        try {
            //getPaths()
            getPath(tsp(0,1))
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getPaths() {
        println("tsp start")
        tsp(n-1,0,1)
        println("tsp end")
        //endCalculate(city.toTypedArray())
        return
    }
    private fun tsp(node: Int, costSum: Int, count: Int){
        println("$node $costSum $count")
        visited[node] = true
        city[count-1] = node
        if(count == n){
            if(costSum < minCost) {
                for (i in 0 until n) {
                    minCity[i] = city[i]
                }
                minCost = costSum
            }
            visited[node] = false
            city[count-1] = -1
            return
        }
        for(i in 0 until n){
            if(!visited[i] && W[node][i] != 0)
                tsp(i, costSum+W[node][i], count+1)
        }
        visited[node] = false
        city[count-1] = -1
    }

    private fun tsp(cur: Int, visited: Int): Int{
        if(visited == ((1).shl(n) - 1)) return dist[cur][0]
        if(cache[cur][visited] != 0) return cache[cur][visited]

        cache[cur][visited] = MAX_VALUE

        for(next in 0..n){
            if(visited.and((1).shl(next)) != 0) continue
            if(dist[cur][next] == 0) continue
            cache[cur][visited] = min(
                cache[cur][visited],
                (tsp(next, visited.or((1).shl(next))) + dist[cur][next])
            )
        }
        return cache[cur][visited]
    }
    private fun getPath(dis: Int){
        var distance = dis
        var piv = 0
        var masking = 1
        path.add(0)
        for(j in 0..n)
            for(k in 0..n){
                if(masking.and((1).shl(k)) != 0) continue
                if((distance - dist[piv][k]) == cache[k][masking + (1).shl(k)]){
                    path.add(k)
                    distance = cache[k][masking + (1).shl(k)]
                    piv = k
                    masking += (1).shl(k)
                    break
                }
            }

        val tmpList = MutableList(n) { i -> i }
        path.forEach { i -> tmpList.remove(i) }
        path.add(tmpList.first())

        endCalculate(path)
    }
    interface listener
}