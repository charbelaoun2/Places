package com.example.places

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceApi {
    @Headers("Authorization:${BuildConfig.FOUR_SQUARE_API}")
    @GET("v3/places/search")
    suspend fun getPlaces(
        @Query("ll") ll: String,
        @Query("radius") radius: Int
    ): Response<PlaceResponse>
}

interface PhotoApi {
    @Headers("Authorization:${BuildConfig.FOUR_SQUARE_API}")
    @GET("v3/places/{fsq_id}/photos")
    suspend fun getPhoto(
        @Path(value="fsq_id") fsq_id:String,
    ):Response<PhotoResponse>
}