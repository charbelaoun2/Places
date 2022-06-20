package com.example.places.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.places.PhotoResponse
import com.example.places.Place
import com.example.places.RetrofitInstance
import kotlinx.coroutines.launch

class PlacesViewModel : ViewModel() {
    var placesLiveData = MutableLiveData<List<Place>>()
    var selectedPlace = MutableLiveData<Place>()

    fun getPlaces() {

        viewModelScope.launch {
            val lisPhoto = mutableListOf<PhotoResponse?>()

            val response = RetrofitInstance.api.getPlaces("41.8781,-87.6298", 9999)
            val responseBody = response.body()?.results
            if (responseBody != null) {
                for (res in responseBody) {
                    val response_photo = RetrofitInstance.api_photo.getPhoto(res.fsq_id)
                    if (response_photo.body() != null) {
                        lisPhoto.add(response_photo.body())
                    }
                }

                val list = responseBody.map {
                    val response_photo = RetrofitInstance.api_photo.getPhoto(it.fsq_id)
                    val photo = lisPhoto.find { e -> e == response_photo.body() }



                    Place(
                        it.fsq_id,
                        it.name,
                        it.location.address,
                        photo?.get(0)?.prefix + "original" + photo?.get(0)?.suffix,
                        it.email,
                        it.description,
                        it.tel,
                        it.geocodes.main.latitude,
                        it.geocodes.main.longitude
                    )

                }
                placesLiveData.value = list
            }
        }
    }
}




