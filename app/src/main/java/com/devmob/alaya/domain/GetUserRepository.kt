package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.User

interface GetUserRepository {
    suspend fun getUser(email: String): User?
    suspend fun updateUserField(userId: String, fieldName: String, fieldValue: Any)
    suspend fun addNewField (userId: String, fieldName: String, newField: Any)
    suspend fun addNewFieldToList (userId: String, fieldName: String, newField: Any)
    suspend fun sendInvitation(email: String, professionalEmail: String): Result<Unit>
    suspend fun getInvitations(professionalEmail: String): Result<List<Invitation>>
    suspend fun updateProfessionalInvitationList(
        professionalEmail: String,
        patientEmail: String,
        status: InvitationStatus
    )
}