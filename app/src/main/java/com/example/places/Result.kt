package com.example.places

data class PlaceResponse(
    val results: List<Result>
)

data class Result(
    val fsq_id : String,
    val geocodes : Geocodes,
    val categories: List<Category>,
    val name : String,
    val email: String,
    val location: Location,
    val tel: String,
    val description: String
)

data class Category(
    val name: String
)

data class Location(
    val address: String
)

data class Geocodes(
    val main : Main,
)

data class Main(
    val latitude : String,
    val longitude : String
)