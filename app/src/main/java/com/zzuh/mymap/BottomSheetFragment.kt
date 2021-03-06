package com.zzuh.mymap

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zzuh.mymap.databinding.FragmentBottomSheetBinding

class BottomSheetFragment : Fragment() {

    class BottomSheetPagerAdapter(activity: FragmentActivity, fragmentList: List<Fragment>)
        : FragmentStateAdapter(activity){
        val fragments: List<Fragment> = fragmentList
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    // fragment ui 변수
    lateinit var binding: FragmentBottomSheetBinding
    lateinit var bottomSheetPagerAdapter: BottomSheetPagerAdapter
    lateinit var bottomSheetActivity: FragmentActivity
    lateinit var bottomLifecycle: Lifecycle

    // view pager에 이용될 fragment
    lateinit var selectedListFragment: SelectedListFragment
    lateinit var resultFragment: ResultFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBottomSheetBinding.inflate(layoutInflater)

        selectedListFragment = SelectedListFragment()
        resultFragment = ResultFragment()
        bottomLifecycle = lifecycle

        // 중첩스크롤 다시보기
        bottomSheetPagerAdapter = BottomSheetPagerAdapter(bottomSheetActivity, listOf(selectedListFragment, resultFragment))
        binding.bottomViewPager.offscreenPageLimit = 2
        binding.bottomViewPager.adapter = bottomSheetPagerAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.bottomSheetActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    fun uiClear(){
        resultFragment.uiClear()
        selectedListFragment.uiClear()
    }
}