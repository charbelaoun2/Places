package com.example.places

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val fsq_id: String?,
    val name: String?,
    val address: String?,
    val email: String?,
    val description: String?,
    val tel: String?,
    val latitude : String,
    val longitude : String,
    val imageUrl: String? = "",
) : Parcelable

