package com.example.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.places.databinding.ListFragmentBinding
import com.example.places.databinding.ListItemBinding
import com.example.places.viewmodels.PlacesViewModel

class ListFragment : Fragment(R.layout.list_fragment), ListAdapter.OnItemClickListener {
    private var binding: ListFragmentBinding? = null
    private lateinit var binding2 : ListItemBinding

    private val viewModel by activityViewModels<PlacesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        binding2 = ListItemBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.placesLiveData.observe(viewLifecycleOwner) { places ->
            setupAdapter(places) }

        viewModel.placesLiveData.observe(viewLifecycleOwner) { places ->
            for (place in places) {
              Glide.with(this)
                  .load(place.imageUrl)
                  .override(600,600)
                  .into(binding2.placeImageView)
            }
        }

    }

    fun setupAdapter(placesList: List<Place>) {
        val listAdapter = ListAdapter(
            placesList, this
        )
        binding?.placesRecyclerView?.adapter = listAdapter
        binding?.placesRecyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.placesRecyclerView?.addItemDecoration(DividerItemDecoration(this.context,DividerItemDecoration.VERTICAL))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(place: Place) {
        viewModel.selectedPlace.value = place
    }
}