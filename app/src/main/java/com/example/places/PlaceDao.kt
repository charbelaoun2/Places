package com.example.places

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlace(place: Place)

    @Query("SELECT * FROM Place_Table")
    fun readAllSavedData() : LiveData<MutableList<Place>>

    @Update
    suspend fun updatePlace(place: Place)

    @Delete
    suspend fun deletePlace(place: Place)
}