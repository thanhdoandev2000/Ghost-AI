package com.example.ghostai.di

import com.example.ghostai.data.repositories.auth.AuthRepositoryImpl
import com.example.ghostai.data.repositories.auth.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): IAuthRepository

}