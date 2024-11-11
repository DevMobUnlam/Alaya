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
            val crisisSteps = getDataFromRemoteDatabase(patientEmail)
            crisisSteps?.let { updateLocalDatabase(it) }
            return crisisSteps
        } else {
            return getDataFromLocalDatabase()
        }
    }

    suspend fun getDataFromRemoteDatabase(patientEmail: String): List<OptionTreatment>? {
        return CustomTreatmentRepository.getCustomTreatment(patientEmail)
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