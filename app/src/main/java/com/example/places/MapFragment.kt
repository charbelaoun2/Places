package com.example.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.places.databinding.MapFragmentBinding
import com.example.places.viewmodels.PlacesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(R.layout.map_fragment), OnMapReadyCallback {
    private var binding: MapFragmentBinding? = null
    private val viewModel by activityViewModels<PlacesViewModel>()
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            googleMap = it
            viewModel.placesLiveData.observe(this) { places ->
                markerPlaces(places)
            }
            viewModel.selectedPlace.observe(this) { place ->
                val location = LatLng(place.latitude.toDouble(),place.longitude.toDouble())
                zoomPlace(location)
            }
        }
    }
    private fun markerPlaces(placesList: List<Place>) {
        val locationFirstPlace = LatLng(placesList[0].latitude.toDouble(), placesList[0].longitude.toDouble())
        zoomPlace(locationFirstPlace)
        for (place in placesList) {
            val location = LatLng(place.latitude.toDouble(),place.longitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(location))
        }
    }

    private fun zoomPlace(location : LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 50f))
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