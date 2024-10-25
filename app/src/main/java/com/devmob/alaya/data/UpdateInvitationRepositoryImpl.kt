package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.UpdateInvitationRepository
import com.devmob.alaya.domain.model.FirebaseResult
import kotlinx.coroutines.tasks.await

class UpdateInvitationRepositoryImpl: UpdateInvitationRepository {
    private val db = FirebaseClient().db

    override suspend fun updateInvitation(email: String, status: String): FirebaseResult {
        return runCatching {
            db.collection("invitations").document(email).update("status", status).await()
        }.toResponseFirebase()
    }
}