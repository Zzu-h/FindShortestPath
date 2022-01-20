package com.zzuh.mymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zzuh.mymap.databinding.ActivityMainBinding

class MainRecyclerViewAdapter(activity: MainActivity, fragmentList: List<Fragment>): FragmentStateAdapter(activity){
    val fragments = fragmentList
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment =fragments[position]
}

class MainActivity : AppCompatActivity() {
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentRecyclerViewAdapter: MainRecyclerViewAdapter
    lateinit var binding: ActivityMainBinding

    lateinit var resultFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()

        fragmentRecyclerViewAdapter = MainRecyclerViewAdapter(this, listOf())

        // Result page 추가
        resultFragment = ResultFragment()
        fragmentTransaction.add(R.id.mainFrameLayout, resultFragment)
        fragmentTransaction.commit()

        // button 추가

        setContentView(binding.root)
    }
}