package com.example.places

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class PlaceDatabase : RoomDatabase() {

    abstract fun placeDao() : PlaceDao


}