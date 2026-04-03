package com.example.alarm.di

import android.content.Context
import androidx.room.Room
import com.example.alarm.data.database.AlarmDatabase
import com.example.alarm.data.database.dao.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAlarmDatabase(
        @ApplicationContext context: Context
    ): AlarmDatabase {
        return Room.databaseBuilder(
            context,
            AlarmDatabase::class.java,
            AlarmDatabase.NAME
        ).build()
    }

    @Provides
    fun providePhotoDao(database: AlarmDatabase): PhotoDao {
        return database.photoDao()
    }
}
