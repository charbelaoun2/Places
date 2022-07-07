package com.example.places

import android.content.Context
import androidx.room.Room

object Manager {

    private lateinit var db: PlaceDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context.applicationContext,
            PlaceDatabase::class.java,
            "place_database"
        ).build()
    }

    suspend fun insertPlace(place: Place) {
        db.placeDao().addPlace(place)
    }
}