package com.devmob.alaya.domain

import com.devmob.alaya.data.GetInvitationProfessionalRepositoryImpl
import com.devmob.alaya.domain.model.Invitation

class GetInvitationUseCase {
    private val getInvitationProfessionalRepository = GetInvitationProfessionalRepositoryImpl()

    suspend operator fun invoke(email: String): Invitation? {
        return getInvitationProfessionalRepository.getInvitationProfessional(email)
    }
}
