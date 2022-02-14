- 현재 구현해야 할 것들 정리

- 아래 처리 전 해야할 것.
1. 로딩화면 구현

—————
- 하단은 검색을 눌렀을 때 처리해야할 과정을 서술

1. n개의 node에 대해서 n개의 path를 받아옴
2. Kruscal algorithm 을 이용 최단 거리를 구한다.
	- 단, 시작점은 무조건 현재 위치
3. 정해진 Path에 대해서
	1. map상에 그림을 그림
	2. 리사이클러뷰로 상세 설명을 추가함

4. 또한 위 모든 과정은 로딩중 화면을 띄우며 3번 과정에서 로딩중 화면을 종료함

—————
- 이 앱을 만들면서 사용된거
    -    Retrofit2
    -    Gson
    -    Naver map api
    -    Fragment
    -    Viewpager2
    -    Recycler view
    -    Bottom sheet
    -    Service component
    -    Tsp 문제: boj 2098
-    사용안한거
    -    Coroutine 
-    추가 라이브러리
    -    Kotlinx-serialization vs. gson
    -    View Binding
    -    Data Binding
    