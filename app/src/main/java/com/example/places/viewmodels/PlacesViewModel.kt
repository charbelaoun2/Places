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
    private var _isLoaded = MutableStateFlow(true)
    val isLoaded = _isLoaded.asStateFlow()

    fun getPlaces() {
        viewModelScope.launch {
            val listPhoto = mutableListOf<PhotoResponse?>()
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
               _isLoaded.value = false
            }
            getPhotoApi(listPhoto, response)
        }
    }

    private fun getPhotoApi(
        listPhoto: MutableList<PhotoResponse?>,
        response: Response<PlaceResponse>
    ) {
        viewModelScope.launch {
            val responseBody = response.body()?.results
            if (responseBody != null) {
                for (res in responseBody) {
                    val responsePhoto = RetrofitInstance.api_photo.getPhoto(res.fsq_id)
                    if (responsePhoto.body() != null) {
                        listPhoto.add(responsePhoto.body())
                    }
                }

                val listPlace = responseBody.map {
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
