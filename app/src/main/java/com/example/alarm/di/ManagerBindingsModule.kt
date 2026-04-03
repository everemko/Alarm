package com.example.alarm.di

import com.example.alarm.domain.repository.LocationRepository
import com.example.alarm.domain.repository.PermissionRepository
import com.example.alarm.data.repository.LocationRepositoryImpl
import com.example.alarm.data.repository.PermissionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerBindingsModule {

    @Binds
    @Singleton
    abstract fun bindPermissionManager(
        impl: PermissionRepositoryImpl
    ): PermissionRepository

    @Binds
    @Singleton
    abstract fun bindLocationManager(
        impl: LocationRepositoryImpl
    ): LocationRepository
}
