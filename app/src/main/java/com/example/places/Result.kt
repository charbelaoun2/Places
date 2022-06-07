package com.example.places

data class Result(
    val categories: List<Category>,
    val name : String,
    val email: String,
    val location: Location,
    val tel: String,
    val description: String
)