package com.example.places

import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.places.databinding.ActivityMainBinding
import com.example.places.viewmodels.PlacesViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<PlacesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepVisibleCondition{
                viewModel.placesLiveData.value==null
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getPlaces()
        loadFragment(ListFragment(), "list fragment")

        binding.listButton.setOnClickListener {
            loadFragment(ListFragment(), "list fragment")
        }

        binding.mapButton.setOnClickListener {
            loadFragment(MapFragment(), "map fragment")
        }

        viewModel.selectedPlace.observe(this) { place ->
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