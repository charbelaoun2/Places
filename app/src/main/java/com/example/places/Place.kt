package com.example.places

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Place_Table")
data class Place(
    @PrimaryKey
    val fsq_id: String,
    val name: String?,
    val address: String?,
    val email: String?,
    val description: String?,
    val tel: String?,
    val latitude : String,
    val longitude : String,
    val imageUrl: String? = "",
) : Parcelable

