package com.devmob.alaya.domain

import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.model.OptionTreatment

class GetCrisisTreatmentUseCase(
    private val prefs: SharedPreferences,
    private val userRepository: GetUserRepository
) {
    suspend operator fun invoke(): List<OptionTreatment>? {
        val email = prefs.getEmail()
        val user = email?.let { userRepository.getUser(it) }
        return user?.stepCrisis
    }
}