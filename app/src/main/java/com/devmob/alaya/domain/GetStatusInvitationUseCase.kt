package com.devmob.alaya.domain

import com.devmob.alaya.data.GetInvitationProfessionalRepositoryImpl

class GetStatusInvitationUseCase {
    private val getInvitationProfessionalRepositoryI = GetInvitationProfessionalRepositoryImpl()

    suspend operator fun invoke(email: String): String? {
        return getInvitationProfessionalRepositoryI.getInvitationProfessional(email)?.status
    }
}