package com.example.ghostai.di

import com.example.ghostai.data.datasources.remote.AppDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAppDataSource(
        appDataSource: AppDataSource
    ): AppDataSource
}