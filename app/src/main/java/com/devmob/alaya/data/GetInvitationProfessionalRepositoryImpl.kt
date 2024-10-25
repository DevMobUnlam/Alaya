package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toInvitation
import com.devmob.alaya.domain.GetInvitationProfessionalRepository
import com.devmob.alaya.domain.model.Invitation
import kotlinx.coroutines.tasks.await

class GetInvitationProfessionalRepositoryImpl : GetInvitationProfessionalRepository {
    private val db = FirebaseClient().db

    override suspend fun getInvitationProfessional(email: String): Invitation? = runCatching {
        db.collection("invitations").document(email).get().await()
    }.toInvitation()
}