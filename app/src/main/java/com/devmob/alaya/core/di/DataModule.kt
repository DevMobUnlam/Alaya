package com.devmob.alaya.core.di

import com.devmob.alaya.data.CrisisRepositoryImpl
import com.devmob.alaya.data.DailyActivityRepositoryImpl
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.ChangeDailyActivityStatusUseCase
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.DailyActivityRepository
import com.devmob.alaya.domain.GetPatientDailyActivitiesUseCase
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.PatientDailyActivitiesUseCases
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
        return CrisisRepositoryImpl(providesFirebaseClient())
    }

    @Provides
    @Singleton
    fun provideDailyActivityRepository(firebaseClient: FirebaseClient): DailyActivityRepository {
        return DailyActivityRepositoryImpl(firebaseClient)
    }



    @Provides
    @Singleton
    fun provideGetUserRepository(): GetUserRepository {
        return GetUserRepositoryImpl(providesFirebaseClient())
    }

    @Provides
    @Singleton
    fun providesFirebaseClient(): FirebaseClient {
        return FirebaseClient()
    }

}