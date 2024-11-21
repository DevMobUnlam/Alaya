package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult
import javax.inject.Inject

class ChangeDailyActivityStatusUseCase @Inject constructor(
    private val repository: DailyActivityRepository
) {

    suspend operator fun invoke(newStatus: Boolean, dailyActivity: DailyActivity, updatedProgress: Int): FirebaseResult {
        return repository.changeDailyActivityStatus(dailyActivity, newStatus, updatedProgress)
    }
}