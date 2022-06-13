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
    lateinit var googleMap: GoogleMap

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

    }

    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            googleMap = it
            viewModel.placesLiveData.observe(this, { places ->
                markerPlaces(places)
            })

        }
    }

    fun markerPlaces(placesList: List<Place>) {
        for (place in placesList) {
            if (place.latitude != null && place.longitude != null) {
                val location = LatLng(place.latitude.toDouble(), place.longitude.toDouble())
                googleMap.addMarker(MarkerOptions().position(location))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
            }

        }

    }

}