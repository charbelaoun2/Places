package com.example.places.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.places.*
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException
import java.net.UnknownHostException

class PlacesViewModel : ViewModel() {
    var placesLiveData = MutableLiveData<List<Place>>()
    var selectedPlace = MutableLiveData<Place>()
    var exceptionCatched = MutableLiveData<Boolean>()

    fun insertDataToDatabase(place: Place) {
        viewModelScope.launch {
            Repository.insertPlace(place)
        }
    }

    fun getPlaces(minParam : Int?,maxPriceParam : Int?,limitParam : Int?,openAtParam : String?,openNow : Boolean?,sort : String?) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPlaces("41.8781,-87.6298", 9999,minParam,maxPriceParam,
                    limitParam,openAtParam,openNow,sort)
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

            } catch (exception: UnknownHostException) {
                exceptionCatched.value = true
            }
        }

    }

    private fun getPhotoApi(
        placeResponse: List<Result>
    ) {
        // TODO: Check this on Thursday
        viewModelScope.launch {
            try {
                val listPhoto = mutableListOf<PhotoResponse?>()
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

            } catch (exception : IndexOutOfBoundsException) {
            }
        }
    }
}
