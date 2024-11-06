package com.devmob.alaya.core.di

import com.devmob.alaya.data.CrisisRepositoryImpl
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.GetUserRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideCrisisRepository(): CrisisRepository {
        return CrisisRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGetUserRepository(): GetUserRepository {
        return GetUserRepositoryImpl()
    }
}