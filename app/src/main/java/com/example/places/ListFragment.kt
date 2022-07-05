package com.example.places

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.places.databinding.LayoutFilterBinding
import com.example.places.databinding.ListFragmentBinding
import com.example.places.databinding.PlaceDetailsBinding
import com.example.places.viewmodels.PlacesViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase


class ListFragment : Fragment(R.layout.list_fragment), ListAdapter.OnItemClickListener {
    private var binding: ListFragmentBinding? = null
    private lateinit var binding3: LayoutFilterBinding
    private lateinit var binding2: PlaceDetailsBinding
    private var swipeBackground : ColorDrawable = ColorDrawable(Color.parseColor("#0371C9"))
    private lateinit var saveIcon : Drawable
    private lateinit var listAdapter: ListAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val viewModel by activityViewModels<PlacesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        binding2 = PlaceDetailsBinding.inflate(inflater, container, false)
        binding3 = LayoutFilterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        saveIcon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_save_24)!!
        viewModel.placesLiveData.observe(viewLifecycleOwner) { places ->
            setupAdapter(places)
            updateRecyclerView(places)
        }
        binding?.filterButton?.setOnClickListener {
            showFilterDialogFragment()
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
        val swipeToSaveCallback = object : SwipeGesture() {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.adapterPosition
                listAdapter.notifyItemChanged(viewHolder.adapterPosition)
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - saveIcon.intrinsicHeight) /2


                if (dX > 0) {
                    swipeBackground.setBounds(itemView.left,itemView.top,dX.toInt(),itemView.bottom)
                    saveIcon.setBounds(itemView.left + iconMargin,itemView.top + iconMargin,
                    itemView.left + iconMargin + saveIcon.intrinsicWidth,itemView.bottom - iconMargin)
                } else {
                    swipeBackground.setBounds(itemView.right+dX.toInt(),itemView.top,itemView.right,itemView.bottom)
                    saveIcon.setBounds(itemView.right - iconMargin - saveIcon.intrinsicWidth,itemView.top + iconMargin,
                        itemView.right - iconMargin ,itemView.bottom - iconMargin)
                }
                swipeBackground.draw(c)
                if ( dX >0 ) {
                    c.clipRect(itemView.left,itemView.top,dX.toInt(),itemView.bottom)
                } else {
                    c.clipRect(itemView.right+dX.toInt(),itemView.top,itemView.right,itemView.bottom)
                }
                saveIcon.draw(c)
                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

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
        if (place.fsq_id != null) {
            firebaseAnalytics.logEvent(Analytics.PLACE_LIST_CLICK) {
                param(FirebaseAnalytics.Param.ITEM_ID, place.fsq_id)
                param(FirebaseAnalytics.Param.ITEM_NAME, place.name ?: "null")
            }
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
}