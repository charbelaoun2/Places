package com.example.places

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.places.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(ListFragment(), "list fragment")

        binding.listButton.setOnClickListener {
            loadFragment(ListFragment(), "list fragment")
        }

        binding.mapButton.setOnClickListener {
            loadFragment(MapFragment(), "map fragment")
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment, tag)
            commit()
        }
    }
}