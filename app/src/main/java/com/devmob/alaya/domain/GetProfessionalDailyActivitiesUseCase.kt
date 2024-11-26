package com.devmob.alaya.domain

import com.devmob.alaya.data.DailyActivityNetwork
import com.devmob.alaya.data.mapper.toDomain
import com.devmob.alaya.domain.model.DailyActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class GetProfessionalDailyActivitiesUseCase(
    private val repository: DailyActivityRepository
) {

    suspend operator fun invoke(patientID: String): Flow<List<DailyActivity>?> {
        return repository.getAssignedDailyActivities(patientID).map{ list ->
            list?.map { item ->
                if(item.lastCompleted != null){
                    val calendar = Calendar.getInstance()
                    val actualDate = calendar.get(Calendar.DAY_OF_YEAR)
                    calendar.time = item.lastCompleted
                    val lastDate = calendar.get(Calendar.DAY_OF_YEAR)

                    if(actualDate == lastDate){
                        item.copy().toDomain(completedToday = true)
                    }else{
                        item.copy().toDomain(completedToday = false)
                    }
                }else{
                    item.copy().toDomain(completedToday = false)
                }
            }  ?: emptyList()
        }
    }

}
