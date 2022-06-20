package com.example.places

class PhotoResponse : ArrayList<ResultPhoto>()

data class ResultPhoto(
    val id: String,
    val prefix: String,
    val suffix: String,
)
