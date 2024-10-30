package com.devmob.alaya.core.di

import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.GetIASummaryUseCase
import com.devmob.alaya.domain.GetUserRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetIASummaryUseCase(gson: Gson, crisisRepository: CrisisRepository, getUserRepository: GetUserRepository): GetIASummaryUseCase {
        return GetIASummaryUseCase(gson, crisisRepository, getUserRepository)
    }
}