package com.devmob.alaya.core.di

import android.content.Context
import com.devmob.alaya.domain.ChangeDailyActivityStatusUseCase
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.DailyActivityRepository
import com.devmob.alaya.domain.GetIASummaryUseCase
import com.devmob.alaya.domain.GetPatientDailyActivitiesUseCase
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.PatientDailyActivitiesUseCases
import com.devmob.alaya.domain.SavePostActivityCommentUseCase
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun providesGenerativeModel(): GenerativeModel{
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyBFGQTD_CB_Yoog-EvnDHkP1RjpFYAQZDs")
    }

    @Provides
    @Singleton
    fun provideGetIASummaryUseCase(gson: Gson, generativeModel: GenerativeModel, crisisRepository: CrisisRepository, getUserRepository: GetUserRepository): GetIASummaryUseCase {
        return GetIASummaryUseCase(gson, generativeModel, crisisRepository, getUserRepository)
    }

    @Provides
    @Singleton
    fun providePatientDailyActivitiesUseCases(dailyActivityRepository: DailyActivityRepository): PatientDailyActivitiesUseCases {
        return PatientDailyActivitiesUseCases(
            changeDailyActivityStatusUseCase = ChangeDailyActivityStatusUseCase(dailyActivityRepository),
            getPatientDailyActivitiesUseCase = GetPatientDailyActivitiesUseCase(dailyActivityRepository),
            savePostActivityCommentUseCase = SavePostActivityCommentUseCase((dailyActivityRepository))
        )
    }
}