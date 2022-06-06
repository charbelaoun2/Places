package com.example.places

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    val api : PlaceApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://developer.foursquare.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceApi::class.java)

    }
}