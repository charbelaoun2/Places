package com.example.places

import android.os.Environment.getExternalStorageDirectory
import android.widget.Toast
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomMasterTable.TABLE_NAME
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import kotlin.coroutines.coroutineContext

@Database(entities = [Place::class], version = 10, exportSchema = false)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao() : PlaceDao
}
