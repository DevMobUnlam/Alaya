package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.FirebaseResult

interface UpdateInvitationRepository {
    suspend fun updateInvitation(email: String, status: String): FirebaseResult
}