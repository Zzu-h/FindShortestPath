package com.example.kotlinproject
var n = 0
val visited = BooleanArray(16){false}
val city = IntArray(16){ -1 }
var minCost = 987654321
val minCity = IntArray(16){ -1 }

// node is start point
fun tsp(node: Int, costSum: Int, count: Int,  W : Array<IntArray>){
    visited[node] = true
    city[count-1] = node
    if(count == n){
        if(costSum < minCost) {
            for (i in 0..(n - 1)) {
                minCity[i] = city[i]
            }
            minCost = costSum
        }
        visited[node] = false
        city[count-1] = -1
        return
    }
    for(i in 0..(n-1)){
        if(!visited[i] && W[node][i] != 0)
            tsp(i, costSum+W[node][i], count+1, W)
    }
    visited[node] = false
    city[count-1] = -1
}

fun main() = with(System.out.bufferedWriter()){
    val br = System.`in`.bufferedReader()
    n = br.readLine().toInt()
    var W = Array(n){
        br.readLine().split(' ').map{it.toInt()}.toIntArray()
    }
    tsp(n-1,0,1, W)
    for(i in 0..(n-1)){
        print("${minCity[i]} ")
    }
    println(" : ${minCost} ")
}
/*
4
0 10 15 20
5 0 9 10
6 13 0 12
8 8 9 0

3 0 1 2  : 27
3 0 2 1  : 36
3 1 0 2  : 28
3 1 2 0  : 23
3 2 0 1  : 25
3 2 1 0  : 27
*/

/*

import java.util.*
import kotlin.math.*

var n = 0
val INF = 987654321
val dp = Array(16){IntArray(1 shl 16){-1} }
var _path = Array(16){
    Array(1 shl 16){
        mutableListOf<Int>()
    }
}
fun dfs(node : Int, state : Int, endState : Int, graph : Array<IntArray>) : Int{

    //모든 도시를 방문한 경우 (종료 조건)
    if(state == endState){
        //마지막 길이 존재하는 경우 마지막 길을 더해준다.
        if(graph[node][0]!=0){
            return graph[node][0]
        }
        //마지막 길이 없는 경우 INF를 넣어주어 아래 min을 통해 제외되게 만든다
        else{
            return INF
        }
    }
    //이미 state 방문 상태로 node를 방문한 경우 재활용
    if(dp[node][state]!=-1) return dp[node][state]
    //min을 구하기 위한 전처리
    dp[node][state] = INF
    _path[node][state].addAll(_path[node][state and (1 shl node).inv()])
    var minNode = 0
    var flag = false
    for(i in 0 until n){
        //이미 방문한 도시이거나 가는 길이 없는 도시면 패스
        if(state and (1 shl i) != 0 || graph[node][i]==0) continue
        //다음 길 누적
        var dfsTemp = dfs(i,state or (1 shl i),endState, graph)
        dp[node][state] =
            if(dp[node][state]<graph[node][i]+dfsTemp){
                dp[node][state]
            }
            else{
                if(flag) _path[node][state].removeLast()
                _path[node][state].add(i)
                flag = true
                graph[node][i] + dfsTemp
            }
    }
    print("$minNode ")
    //정답 반환
    return dp[node][state]
}

fun main() = with(System.out.bufferedWriter()){
    val br = System.`in`.bufferedReader()
    n = br.readLine().toInt()
    var graph = Array(n){
        br.readLine().split(' ').map{it.toInt()}.toIntArray()
    }
    var endState = (1 shl n) -1
    _path[0][1].add(0)
    write("\n${dfs(0,1,endState,graph)}")
    for(item in _path[0][endState])
        print("$item ")

    close()
}*/
