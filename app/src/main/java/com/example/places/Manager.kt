package com.example.places

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room

object Manager {

    private lateinit var db: PlaceDatabase
    private lateinit var readAllData : LiveData<List<Place>>

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
    fun readAllSavedData(): LiveData<List<Place>> {
        readAllData = db.placeDao().readAllSavedData()
        return readAllData
    }
}