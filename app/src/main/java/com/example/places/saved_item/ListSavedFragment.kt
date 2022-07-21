package com.example.places.saved_item

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.places.Manager
import com.example.places.Place
import com.example.places.R
import com.example.places.databinding.FragmentSavedBinding
import com.example.places.viewmodels.PlacesViewModel

class ListSavedFragment : Fragment(R.layout.fragment_saved) {

    private val viewModel by viewModels<PlacesViewModel>()
    private lateinit var listAdapter: ListSavedItemAdapter
    private lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter = ListSavedItemAdapter()
        binding.placeSavedRecyclerView.adapter = listAdapter
        binding.placeSavedRecyclerView.layoutManager = LinearLayoutManager(context)

        val swipeToDeleteCallback = object : SwipeDelete(requireContext()) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val placeSwiped = listAdapter.itemListSaved[viewHolder.adapterPosition]
                viewModel.deletePlaceDatabase(placeSwiped)
                listAdapter.notifyItemChanged(viewHolder.adapterPosition)
            }
        }
        val itemTouchDelete = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchDelete.attachToRecyclerView(binding.placeSavedRecyclerView)

        val swipeToSaveCallback = object : SwipeEdit(requireContext()) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val placeSwiped = listAdapter.itemListSaved[viewHolder.adapterPosition]
                showEditDialogFragment(placeSwiped)
                viewModel.editedText.observe(viewLifecycleOwner) {
                    try {
                        listAdapter.itemListSaved[viewHolder.adapterPosition] = it
                        listAdapter.notifyItemChanged(viewHolder.adapterPosition)
                        viewModel.updatePlaceDatabase(it)
                    } catch (exception: ArrayIndexOutOfBoundsException) {
                    }

                }
                listAdapter.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToSaveCallback)
        itemTouchHelper.attachToRecyclerView(binding.placeSavedRecyclerView)

        viewModel.readAllSavedData.observe(viewLifecycleOwner) { place ->
            listAdapter.setData(place)
        }

        binding.exportToCsv.setOnClickListener {
           Manager.exportCSV()
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
            listAdapter.notifyDataSetChanged()
        }
    }

    private fun showEditDialogFragment(place: Place) {
        val oldFragment = childFragmentManager.findFragmentByTag(EditDialogFragment.TAG)
        if (oldFragment != null) {
            childFragmentManager.beginTransaction().remove(oldFragment).commit()
        }

        val fragment = EditDialogFragment.newInstance(place)
        childFragmentManager.beginTransaction()
            .add(fragment, EditDialogFragment.TAG)
            .commit()
    }
}

