package com.example.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var naverMapFragment: NaverMapFragment

    lateinit var transaction: FragmentTransaction
    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        naverMapFragment = NaverMapFragment("l611cwsx3d")

        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()

        transaction.add(R.id.naver_mv, naverMapFragment)
        transaction.commit()
        setContentView(R.layout.activity_main)
    }
}