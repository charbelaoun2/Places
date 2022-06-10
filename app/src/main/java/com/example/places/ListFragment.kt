package com.example.places

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.places.databinding.ListFragmentBinding
import com.example.places.viewmodels.PlacesViewModel

class ListFragment : Fragment(R.layout.list_fragment),ListAdapter.OnItemClickListener{
    private var binding: ListFragmentBinding? = null
    private val viewModel by activityViewModels<PlacesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.placesLiveData.observe(viewLifecycleOwner, { places ->
            setupAdapter(places)
        })
    }

    fun setupAdapter(placesList: List<Place>) {
        val listAdapter = ListAdapter(
            placesList,this
        )

        binding?.placesRecyclerView?.adapter = listAdapter

        binding?.placesRecyclerView?.layoutManager = LinearLayoutManager(context)



    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(place: Place) {
        viewModel.selectedPlace.value=place

    }
}