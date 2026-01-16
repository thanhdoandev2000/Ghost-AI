package com.example.ghostai.data.repositories.auth

import com.example.ghostai.data.datasources.remote.AppDataSource
import jakarta.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val appDataSource: AppDataSource
) : IAuthRepository {
}