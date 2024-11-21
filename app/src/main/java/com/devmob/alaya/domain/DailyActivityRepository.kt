package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface DailyActivityRepository {
    suspend fun getDailyActivities(): Flow<List<DailyActivity>?>
    suspend fun changeDailyActivityStatus(dailyActivity: DailyActivity, newStatus: Boolean, updatedProgress: Int): FirebaseResult
    suspend fun savePostActivityComments(activityId: String, comments: String): FirebaseResult
}