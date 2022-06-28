package com.example.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
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
        viewModel.placesLiveData.observe(viewLifecycleOwner) { places ->
            setupAdapter(places)
        }
        binding?.filterButton?.setOnClickListener {
            showFilterDialog()
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


    private fun showFilterDialog() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.layout_filter)

        dialog.findViewById<Button>(R.id.negative_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.positive_button).setOnClickListener {
            val minPriceInput = dialog.findViewById<EditText>(R.id.min_price_id).text.toString()
            val maxPriceInput = dialog.findViewById<EditText>(R.id.max_price_id).text.toString()
            val limitInput = dialog.findViewById<EditText>(R.id.limit_id).text.toString()
            val openAtInput = dialog.findViewById<EditText>(R.id.open_at_id).text.toString()
            val radioGroupSort = dialog.findViewById<RadioGroup>(R.id.filter_sort)
            val radioGroupOpenNow = dialog.findViewById<RadioGroup>(R.id.filter_open_now)
            if (radioGroupSort.checkedRadioButtonId != -1 && radioGroupOpenNow.checkedRadioButtonId == -1) {
                val radioButtonSort =
                    radioGroupSort.findViewById<View>(radioGroupSort.checkedRadioButtonId) as RadioButton
                viewModel.getPlaces(
                    minPriceInput.toIntOrNull(),
                    maxPriceInput.toIntOrNull(),
                    limitInput.toIntOrNull(),
                    openAtInput,
                    null,
                    radioButtonSort.text.toString()
                )
            } else if (radioGroupSort.checkedRadioButtonId != -1 && radioGroupOpenNow.checkedRadioButtonId != -1) {
                val radioButtonSort =
                    radioGroupSort.findViewById<View>(radioGroupSort.checkedRadioButtonId) as RadioButton
                val radioButtonOpenNow =
                    radioGroupOpenNow.findViewById<View>(radioGroupOpenNow.checkedRadioButtonId) as RadioButton
                viewModel.getPlaces(
                    minPriceInput.toIntOrNull(),
                    maxPriceInput.toIntOrNull(),
                    limitInput.toIntOrNull(),
                    openAtInput,
                    radioButtonOpenNow.text.toString().toBoolean(),
                    radioButtonSort.text.toString()
                )
            } else if (radioGroupSort.checkedRadioButtonId == -1 && radioGroupOpenNow.checkedRadioButtonId != -1) {
                val radioButtonOpenNow: RadioButton =
                    radioGroupOpenNow.findViewById<View>(radioGroupOpenNow.checkedRadioButtonId) as RadioButton
                viewModel.getPlaces(
                    minPriceInput.toIntOrNull(),
                    maxPriceInput.toIntOrNull(),
                    limitInput.toIntOrNull(),
                    openAtInput,
                    radioButtonOpenNow.text.toString().toBoolean(),
                    null
                )
            } else {
                viewModel.getPlaces(
                    minPriceInput.toIntOrNull(),
                    maxPriceInput.toIntOrNull(),
                    limitInput.toIntOrNull(),
                    openAtInput,
                    null,
                    null
                )
            }
            viewModel.placesLiveData.observe(viewLifecycleOwner) { list ->
                updateRecyclerView(list)
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}