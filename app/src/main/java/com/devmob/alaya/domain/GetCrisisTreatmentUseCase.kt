package com.devmob.alaya.domain

import com.devmob.alaya.data.local_storage.CrisisStepsDao
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.model.OptionTreatment

class GetCrisisTreatmentUseCase(
    private val prefs: SharedPreferences,
    private val userRepository: GetUserRepository,
    private val crisisStepsDao: CrisisStepsDao
) {

    suspend operator fun invoke(): List<OptionTreatment>? {
        if (getDataFromLocalDatabase().isEmpty()) {
            val email = prefs.getEmail()
            val stepsCrisis = email?.let { getDataFromRemoteDatabase(it) }
            stepsCrisis?.let { updateLocalDatabase(it) }
            return stepsCrisis
        } else {
            return getDataFromLocalDatabase()
        }
    }

    suspend fun getDataFromRemoteDatabase(patientEmail: String): List<OptionTreatment>? {
        return userRepository.getUser(patientEmail)?.stepCrisis
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