package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.Invitation

interface GetInvitationProfessionalRepository {
    suspend fun getInvitationProfessional(email: String): Invitation?
}
