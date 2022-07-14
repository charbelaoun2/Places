package com.example.places.saved_item

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val swipeToDeleteCallback = object  : SwipeDelete(this) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val placeSwiped = listAdapter.itemListSaved[viewHolder.adapterPosition]
                viewModel.deletePlaceDatabase(placeSwiped)
                listAdapter.notifyItemChanged(viewHolder.adapterPosition)
            }
        }
        val itemTouchDelete = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchDelete.attachToRecyclerView(binding.placeSavedRecyclerView)

        val swipeToSaveCallback = object : SwipeEdit(this) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val placeSwiped = listAdapter.itemListSaved[viewHolder.adapterPosition]
                showEditDialogFragment(placeSwiped)
                viewModel.editedText.observe(this@SavedItemActivity) {
                    try {
                        listAdapter.itemListSaved[viewHolder.adapterPosition] = it
                        listAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        viewModel.updatePlaceDatabase(it)
                    } catch (exception : ArrayIndexOutOfBoundsException) {}

                }
                listAdapter.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToSaveCallback)
        itemTouchHelper.attachToRecyclerView(binding.placeSavedRecyclerView)

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
        updateRecyclerView(filterName as MutableList<Place>?)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(list: MutableList<Place>?) {
        binding.placeSavedRecyclerView.apply {
            if (list != null) {
                listAdapter.itemListSaved = list
            }
            this@SavedItemActivity.listAdapter.notifyDataSetChanged()
        }
    }

    private fun showEditDialogFragment(place: Place) {
        val oldFragment = supportFragmentManager.findFragmentByTag(EditDialogFragment.TAG)
        if (oldFragment != null) {
            supportFragmentManager.beginTransaction().remove(oldFragment).commit()
        }

        val fragment = EditDialogFragment.newInstance(place)
        supportFragmentManager.beginTransaction()
            .add(fragment, EditDialogFragment.TAG)
            .commit()
    }
}