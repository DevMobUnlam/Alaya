package com.devmob.alaya.domain

import com.devmob.alaya.data.mapper.toNetwork
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult

class EditDailyActivityUseCase(
    private val repository: DailyActivityRepository
){
    suspend operator fun invoke(patientID: String, dailyActivity: DailyActivity): FirebaseResult{
        return repository.editDailyActivity(patientID = patientID, dailyActivity = dailyActivity.toNetwork())
    }
}
