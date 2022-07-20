package com.example.places.saved_item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.places.Place
import com.example.places.databinding.ActivitySavedItemBinding
import com.example.places.viewmodels.PlacesViewModel

class SavedItemActivity : AppCompatActivity() {
    private val viewModel by viewModels<PlacesViewModel>()
    private lateinit var listAdapter: ListSavedItemAdapter
    private lateinit var binding: ActivitySavedItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listAdapter = ListSavedItemAdapter()
        binding.placeSavedRecyclerView.adapter = listAdapter
        binding.placeSavedRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.readAllSavedData.observe(this) { place ->
            println(place)
            listAdapter.setData(place)
        }

        binding.nameSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true
            }
        })
    }

    fun search(text: String?) {
        val filterName = viewModel.readAllSavedData.value?.filter {
            it.name?.contains(text.toString(), true) == true
        }
        updateRecyclerView(filterName)
    }

    private fun updateRecyclerView(list: List<Place>?) {
        binding.placeSavedRecyclerView.apply {
            if (list != null) {
                listAdapter.itemListSaved = list
            }
            listAdapter.notifyDataSetChanged()
        }
    }
}