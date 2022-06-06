package com.example.places

import retrofit2.Response
import retrofit2.http.GET


interface PlaceApi {

    @GET("/reference/place-details")
    suspend fun getPlaces(): Response<List<Place>>
}