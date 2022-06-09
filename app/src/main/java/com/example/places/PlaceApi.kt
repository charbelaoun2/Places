package com.example.places

import androidx.lifecycle.MutableLiveData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PlaceApi {
    @Headers("Authorization:${BuildConfig.FOUR_SQUARE_API}")
    @GET("v3/places/search")
    suspend fun getPlaces(
        @Query("ll") ll: String,
        @Query("radius") radius: Int
    ): Response<PlaceResponse>
}