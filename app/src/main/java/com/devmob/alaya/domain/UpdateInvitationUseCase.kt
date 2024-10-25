package com.devmob.alaya.domain

import com.devmob.alaya.data.UpdateInvitationRepositoryImpl

class UpdateInvitationUseCase {
    private val updateInvitationRepository = UpdateInvitationRepositoryImpl()

    suspend operator fun invoke(email: String, status: String) {
        updateInvitationRepository.updateInvitation(email, status)
    }
}