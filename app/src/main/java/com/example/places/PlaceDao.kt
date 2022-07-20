package com.example.places

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlace(place: Place)

    @Query("SELECT * FROM Place_Table ORDER BY fsq_id")
    fun readAllSavedData() : LiveData<MutableList<Place>>

    @Update
    suspend fun updatePlace(place: Place)
}