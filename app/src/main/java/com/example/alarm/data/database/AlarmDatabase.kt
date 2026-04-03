package com.example.alarm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alarm.data.database.dao.PhotoDao
import com.example.alarm.data.database.entity.Photo

@Database(
    entities = [Photo::class],
    version = 1,
    exportSchema = false
)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        const val NAME = "alarm.db"
    }
}
