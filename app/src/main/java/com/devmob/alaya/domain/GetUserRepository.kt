package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.User

interface GetUserRepository {
    suspend fun getUser(email: String): User?
    suspend fun updateUserField(userId: String, fieldName: String, fieldValue: Any)
    suspend fun addNewField (userId: String, fieldName: String, newField: Any)
    suspend fun sendInvitation(invitationForPatient: Invitation, invitationForProfessional: Invitation): Result<Unit>
    suspend fun updateProfileImage(userId: String, imageUrl: String) : Boolean
    suspend fun updatePhoneNumber(userId: String, phoneNumber: String): Boolean
}