package com.example.places.saved_item

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.places.MapFragment
import com.example.places.R
import com.example.places.databinding.ActivitySavedItemBinding
import com.example.places.viewmodels.PlacesViewModel

class SavedItemActivity : AppCompatActivity(){

    private val viewModel by viewModels<PlacesViewModel>()
    private lateinit var binding: ActivitySavedItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySavedItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(ListSavedFragment(), "list saved fragment")


        viewModel.selectedPlace.observe(this) {
            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)
        }

    }


    private fun loadFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout_saved, fragment, tag)
            commit()
        }
    }

}