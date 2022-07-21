package com.example.places

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.places.databinding.LayoutFilterBinding
import com.example.places.databinding.ListFragmentBinding
import com.example.places.databinding.PlaceDetailsBinding
import com.example.places.saved_item.SavedItemActivity
import com.example.places.viewmodels.PlacesViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class ListFragment : Fragment(R.layout.list_fragment), ListAdapter.OnItemClickListener {
    private var binding: ListFragmentBinding? = null
    private lateinit var binding3: LayoutFilterBinding
    private lateinit var binding2: PlaceDetailsBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var placeSwiped: Place
    private val viewModel by activityViewModels<PlacesViewModel>()
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(),
            R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(),
            R.anim.rotate_clode_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(),
            R.anim.from_btn_anim)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(),
            R.anim.to_btn_anim)
    }
    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        binding2 = PlaceDetailsBinding.inflate(inflater, container, false)
        binding3 = LayoutFilterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        viewModel.placesLiveData.observe(viewLifecycleOwner) { places ->
            setupAdapter(places)
            updateRecyclerView(places)
        }

        binding?.openFilter?.setOnClickListener {
            onAddBtnClicked()
        }
        binding?.filterButton?.setOnClickListener {
            showFilterDialogFragment()
        }
        binding?.saveButton?.setOnClickListener {
            val intent = Intent(requireContext(), SavedItemActivity::class.java)
            startActivity(intent)
        }
        binding?.nameSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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
        val filterName = viewModel.placesLiveData.value?.filter {
            it.name?.contains(text.toString(), true) == true
        }
        updateRecyclerView(filterName)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView(list: List<Place>?) {
        binding?.placesRecyclerView.apply {
            if (list != null) {
                listAdapter.lists = list
            }
            listAdapter.notifyDataSetChanged()
        }
    }

    private fun setupAdapter(placesList: List<Place>) {

        listAdapter = ListAdapter(
            placesList, this
        )
        val swipeToSaveCallback = object : SwipeGesture(requireContext()) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.adapterPosition
                placeSwiped = listAdapter.lists[viewHolder.adapterPosition]
                viewModel.readAllSavedData.observe(viewLifecycleOwner) { places ->
                    if (places.isEmpty()) {
                        viewModel.insertDataToDatabase(placeSwiped)
                    }
                    if (places.none {it.name == placeSwiped.name || it.address == placeSwiped.address}) {
                        viewModel.insertDataToDatabase(placeSwiped)
                    }
                }
                listAdapter.notifyItemChanged(viewHolder.adapterPosition)
                Toast.makeText(
                    context,
                    "This Place has been saved to the Database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToSaveCallback)
        itemTouchHelper.attachToRecyclerView(binding?.placesRecyclerView)
        binding?.placesRecyclerView?.adapter = listAdapter
        binding?.placesRecyclerView?.layoutManager = LinearLayoutManager(context)
        binding?.nameSearch?.isSubmitButtonEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(place: Place) {
        firebaseAnalytics.logEvent(Analytics.PLACE_LIST_CLICK) {
            param(FirebaseAnalytics.Param.ITEM_ID, place.fsq_id)
            param(FirebaseAnalytics.Param.ITEM_NAME, place.name ?: "null")
        }
        viewModel.selectedPlace.value = place
    }

    private fun showFilterDialogFragment() {
        val oldFragment = childFragmentManager.findFragmentByTag(PlaceDetailBottomSheet.TAG)
        if (oldFragment != null) {
            childFragmentManager.beginTransaction().remove(oldFragment).commit()
        }

        val fragment = FilterDialogFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(fragment, FilterDialogFragment.TAG)
            .commit()
    }

    private fun onAddBtnClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding?.filterButton?.startAnimation(fromBottom)
            binding?.saveButton?.startAnimation(fromBottom)
            binding?.openFilter?.startAnimation(rotateOpen)
        } else {
            binding?.filterButton?.startAnimation(toBottom)
            binding?.saveButton?.startAnimation(toBottom)
            binding?.openFilter?.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding?.filterButton?.visibility = View.VISIBLE
            binding?.saveButton?.visibility = View.VISIBLE
        } else {
            binding?.filterButton?.visibility = View.INVISIBLE
            binding?.saveButton?.visibility = View.INVISIBLE
        }
    }
}