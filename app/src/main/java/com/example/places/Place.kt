package com.example.places

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Place_Table")
data class Place(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var fsq_id: String,
    var name: String?,
    var address: String?,
    val email: String?,
    val description: String?,
    val tel: String?,
    val latitude : String,
    val longitude : String,
    val imageUrl: String? = "",
) : Parcelable

