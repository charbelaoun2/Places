package com.example.places

data class PlaceResponse(
    val results: List<Result>
)

data class Result(
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