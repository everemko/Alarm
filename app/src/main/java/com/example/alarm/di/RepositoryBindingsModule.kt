package com.example.alarm.di

import com.example.alarm.domain.repository.DialogFlow
import com.example.alarm.domain.repository.NavigationFlow
import com.example.alarm.domain.repository.PhotoFileNameRepository
import com.example.alarm.domain.repository.PhotoFolderRepository
import com.example.alarm.domain.repository.PhotoRepository
import com.example.alarm.navigation.NavigationFlowImpl
import com.example.alarm.navigation.DialogFlowImpl
import com.example.alarm.data.repository.PhotoFileNameFactory
import com.example.alarm.data.repository.PhotoFolderRepositoryImpl
import com.example.alarm.data.repository.PhotoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindingsModule {

    @Binds
    @Singleton
    abstract fun bindDialogFlow(
        impl: DialogFlowImpl
    ): DialogFlow

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        impl: PhotoRepositoryImpl
    ): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindPhotoFolderRepository(
        impl: PhotoFolderRepositoryImpl
    ): PhotoFolderRepository

    @Binds
    @Singleton
    abstract fun bindNavigationFlow(
        impl: NavigationFlowImpl
    ): NavigationFlow

    @Binds
    @Singleton
    abstract fun bindPhotoFileNameFactory(
        impl: PhotoFileNameFactory
    ): PhotoFileNameRepository
}
