package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl

class AddProfessionalToPatientUseCase {
    private val getUserRepository = GetUserRepositoryImpl()

    suspend fun updateUserField(
        userId: String,
        linkedUsers: String,
        professionalEmail: List<String>
    ) {
        getUserRepository.updateUserField(userId, linkedUsers, professionalEmail)
    }
}