package com.example.places

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.places.databinding.MapFragmentBinding
import com.example.places.viewmodels.PlacesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class MapFragment : Fragment(R.layout.map_fragment), OnMapReadyCallback {
    private var binding: MapFragmentBinding? = null
    private val viewModel by activityViewModels<PlacesViewModel>()
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.mapView?.onCreate(savedInstanceState)
        binding?.mapView?.onResume()
        binding?.mapView?.getMapAsync(this)

        viewModel.selectedPlace.observe(viewLifecycleOwner) { selectedPlace ->
            if (selectedPlace != null) {
                showPlaceDetailsBottomSheet(selectedPlace)
            }
        }

        binding?.mapSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = binding!!.mapSearch.query.toString()
                var addressList: List<Address>? = null
                if (location != null || location == "") {

                    val geocoder = Geocoder(requireContext())
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e : NullPointerException) {
                        e.printStackTrace()
                    }
                    val address: Address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(location))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        binding?.mapView?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            googleMap = it

            binding?.ListPlacesButton?.setOnClickListener {
                viewModel.placesLiveData.observe(this) {places->
                    markerPlaces(places)
                }
            }
            binding?.listSavedButton?.setOnClickListener {
                viewModel.readAllSavedData.observe(this) {places->
                    markerPlaces(places)
                }
            }
            viewModel.selectedPlace.observe(this) { place ->
                val location = LatLng(place.latitude.toDouble(),place.longitude.toDouble())
                zoomPlace(location)
                val marker = googleMap.addMarker(MarkerOptions().position(location))
                marker?.tag = place.fsq_id
                googleMap.setOnMarkerClickListener {
                    if (place != null) {
                        showPlaceDetailsBottomSheet(place)
                    }
                    return@setOnMarkerClickListener false
                }
            }
        }
    }
    private fun markerPlaces(placesList: List<Place>) {
        val locationFirstPlace = LatLng(placesList[0].latitude.toDouble(), placesList[0].longitude.toDouble())
        zoomPlace(locationFirstPlace)
        for (place in placesList) {
            val location = LatLng(
                place.latitude.toDouble(),
                place.longitude.toDouble()
            )
            val marker = googleMap.addMarker(MarkerOptions().position(location))
            marker?.tag = place.fsq_id
        }

        googleMap.setOnMarkerClickListener { marker ->
            val selectedPlace = placesList.find { it.fsq_id == marker.tag }
            if (selectedPlace != null) {
                showPlaceDetailsBottomSheet(selectedPlace)
            }
            return@setOnMarkerClickListener false
        }
    }


    private fun zoomPlace(location : LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 20f))
    }

    private fun showPlaceDetailsBottomSheet(place: Place) {
        val oldFragment = childFragmentManager.findFragmentByTag(PlaceDetailBottomSheet.TAG)
        if (oldFragment != null) {
            childFragmentManager.beginTransaction().remove(oldFragment).commit()
        }
        val fragment = PlaceDetailBottomSheet.newInstance(place)
        childFragmentManager.beginTransaction()
            .add(fragment, PlaceDetailBottomSheet.TAG)
            .commit()
    }
}