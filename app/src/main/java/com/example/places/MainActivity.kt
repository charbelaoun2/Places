package com.example.places

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.places.databinding.ActivityMainBinding
import com.example.places.viewmodels.PlacesViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val viewModel by viewModels<PlacesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        installSplashScreen().apply {
            setKeepVisibleCondition{
               viewModel.exceptionCaught.value==false
            }
        }
        viewModel.exceptionCaught.observe(this) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Check your internet connection !")
            builder.setMessage("Enable to fetch Api data due to internet connection Problems ")
            builder.setIcon(R.drawable.ic_baseline_wifi_off_24)
            builder.setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getPlaces(null,null,null,null,null,null)
        loadFragment(ListFragment(), "list fragment")

        binding.bottomNavigationView.setOnItemSelectedListener  {
            when (it.itemId) {
                R.id.List_button -> loadFragment(ListFragment(), "List Fragment")
                R.id.map_button -> loadFragment(MapFragment(), "Map Fragment")
            }
            true
        }
        viewModel.selectedPlace.observe(this) {
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