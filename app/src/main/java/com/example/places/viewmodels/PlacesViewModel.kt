package com.example.places.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.places.Place
import com.example.places.RetrofitInstance
import kotlinx.coroutines.launch


class PlacesViewModel : ViewModel() {

    var placesLiveData = MutableLiveData<List<Place>>()

    fun getPlaces(){

        viewModelScope.launch {
            val response = RetrofitInstance.api.getPlaces("41.8781,-87.6298", 9999)

            val list = response.body()?.results?.map {
                Place(
                    it.name,
                    it.location.address, "url", it.email, it.description, it.tel
                )
            } ?: listOf()

            placesLiveData.value = list
        }
    }

}