package com.example.places

import android.content.Context
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter


object Manager {

    private const val TABLE_NAME = "Place_Table"
    private lateinit var db: PlaceDatabase
    private lateinit var readAllData: LiveData<MutableList<Place>>

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context.applicationContext,
            PlaceDatabase::class.java,
            "place_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    suspend fun insertPlace(place: Place) {
        db.placeDao().addPlace(place)
    }

    fun readAllSavedData(): LiveData<MutableList<Place>> {
        readAllData = db.placeDao().readAllSavedData()
        return readAllData
    }

    suspend fun updatePlace(place: Place) {
        db.placeDao().updatePlace(place)
    }

    suspend fun deletePlace(place: Place) {
        db.placeDao().deletePlace(place)
    }

    fun exportCSV() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "files/test")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        val file = File(exportDir, "to2_places.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val curCSV = db.query("SELECT * FROM $TABLE_NAME", null)
            csvWrite.writeNext(curCSV.columnNames)
            while (curCSV.moveToNext()) {
                val arrStr = arrayOfNulls<String>(curCSV.columnCount)
                for (i in 0 until curCSV.columnCount - 1) {
                    when (i) {
                        20, 22 -> {
                        }
                        else -> arrStr[i] = curCSV.getString(i)
                    }
                }
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            curCSV.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}