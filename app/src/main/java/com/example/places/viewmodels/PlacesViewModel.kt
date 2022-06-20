package com.example.places.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.places.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class PlacesViewModel : ViewModel() {
    var placesLiveData = MutableLiveData<List<Place>>()
    var selectedPlace = MutableLiveData<Place>()

    fun getPlaces() {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getPlaces("41.8781,-87.6298", 9999)
            val placesResponse = response.body()?.results
            if (placesResponse != null) {
                val listPlace = placesResponse.map {
                    Place(
                        it.fsq_id,
                        it.name,
                        it.location.address,
                        it.email,
                        it.description,
                        it.tel,
                        it.geocodes.main.latitude,
                        it.geocodes.main.longitude
                    )
                }
                placesLiveData.value = listPlace
            }
            if (placesResponse != null) {
                getPhotoApi(placesResponse)
            }
        }
    }

    private fun getPhotoApi(
        placeResponse: List<Result>
    ) {
        // TODO: Check this on Thursday
        viewModelScope.launch {
            val listPhoto = mutableListOf<PhotoResponse?>()
            if (placeResponse != null) {
                for (res in placeResponse) {
                    val responsePhoto = RetrofitInstance.api_photo.getPhoto(res.fsq_id)
                    if (responsePhoto.body() != null) {
                        listPhoto.add(responsePhoto.body())
                    }
                }

                val listPlace = placeResponse.map {
                    val responsePhoto = RetrofitInstance.api_photo.getPhoto(it.fsq_id)
                    val photo = listPhoto.find { e -> e == responsePhoto.body() }

                    Place(
                        it.fsq_id,
                        it.name,
                        it.location.address,
                        it.email,
                        it.description,
                        it.tel,
                        it.geocodes.main.latitude,
                        it.geocodes.main.longitude,
                        photo?.get(0)?.prefix + "original" + photo?.get(0)?.suffix,
                    )

                }
                placesLiveData.value = listPlace

            }
        }
    }
}
