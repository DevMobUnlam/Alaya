package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus

class GetInvitationUseCase {
    private val getUserUseCase = GetUserRepositoryImpl()

    suspend fun getInvitationProfessional(email: String): Invitation? {
        return getUserUseCase.getUser(email)?.invitation
    }

    suspend fun updateInvitation(email: String, fieldName: String, status: InvitationStatus) {
        getUserUseCase.updateUserField(email, fieldName, status.name)
    }
}
