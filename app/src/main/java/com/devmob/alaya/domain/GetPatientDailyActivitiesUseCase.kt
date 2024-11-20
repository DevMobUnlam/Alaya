package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.DailyActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPatientDailyActivitiesUseCase {
    operator fun invoke(): Flow<List<DailyActivity>> {
        return flow{
            //TODO COMPLETAR
            emit(listOf(DailyActivity()))
        }
    }
}