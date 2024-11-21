package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.DailyActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPatientDailyActivitiesUseCase @Inject constructor(
    private val repository: DailyActivityRepository
) {
    suspend operator fun invoke(): Flow<List<DailyActivity>?> {
        return repository.getDailyActivities().map {
            it?.filterNot { activity ->
                activity.maxProgress == 0} ?: emptyList()
        }
    }
}