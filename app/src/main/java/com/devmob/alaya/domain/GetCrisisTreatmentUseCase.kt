package com.devmob.alaya.domain

import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.data.local_storage.CrisisStepsDao
import com.devmob.alaya.domain.model.OptionTreatment

class GetCrisisTreatmentUseCase(private val crisisStepsDao: CrisisStepsDao) {

    private val CustomTreatmentRepository = CrisisTreatmentRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String
    ): List<OptionTreatment>? {
        if (getDataFromLocalDatabase().isEmpty()) {
            val result = CustomTreatmentRepository.getCustomTreatment(patientEmail)
            result?.let { updateLocalDatabase(it) }
            return result
        } else {
            return getDataFromLocalDatabase()
        }
    }

    private suspend fun getDataFromLocalDatabase(): List<OptionTreatment> {
        return crisisStepsDao.getCrisisSteps()
    }

    private suspend fun updateLocalDatabase(steps: List<OptionTreatment>) {
        steps.forEach {
            crisisStepsDao.insertCrisisStep(it)
        }
    }
}