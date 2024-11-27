package com.devmob.alaya.domain

import com.devmob.alaya.data.mapper.toNetwork
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult

class CreateDailyActivityUseCase(
    private val repository: DailyActivityRepository
) {
    suspend operator fun invoke(patientID: String, dailyActivity: DailyActivity): FirebaseResult{
        return repository.createDailyActivity(patientID, dailyActivity.toNetwork())
    }
}
