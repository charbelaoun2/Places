package com.example.places

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.places.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, ListFragment())
            commit()
        }

        binding.listButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, ListFragment(), "list fragment")
                commit()
            }
        }

        binding.mapButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout, MapFragment(), "Map Fragment")
                commit()
            }
        }


    }
}