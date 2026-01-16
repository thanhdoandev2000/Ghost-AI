package com.example.ghostai.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.ghostai.data.local.preferences.AppPreferencesImpl
import com.example.ghostai.data.local.preferences.IAppPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ghost_ai_prefs")

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

    @Binds
    @Singleton
    abstract fun bindAppPreferences(appPreferencesImpl: AppPreferencesImpl): IAppPreferences

    companion object {
        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }
}
