package com.devmob.alaya.data

import android.util.Log
import com.devmob.alaya.data.mapper.toUser
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class GetUserRepositoryImpl : GetUserRepository {
    private val db = FirebaseClient().db

    override suspend fun getUser(email: String): User? = runCatching {
        db.collection("users").document(email).get().await()
    }.toUser()

    override suspend fun updateUserField(userId: String, fieldName: String, fieldValue: Any) {
        db.collection("users").document(userId)
            .update(fieldName, fieldValue).await()
    }

    override suspend fun addNewField(userId: String, fieldName: String, newField: Any) {
        db.collection("users").document(userId)
            .set(mapOf(fieldName to newField), SetOptions.merge()).await()
    }

    override suspend fun addNewFieldToList(userId: String, fieldName: String, newField: Any) {
        val document = db.collection("users").document(userId).get().await()
        val currentList = document.get(fieldName) as? List<Any> ?: emptyList()
        val updatedList = currentList + newField
        db.collection("users").document(userId)
            .update(fieldName, updatedList).await()
    }

    override suspend fun sendInvitation(
        patientEmail: String,
        professionalEmail: String
    ): Result<Unit> {
        val invitationForPatient = Invitation(professionalEmail, InvitationStatus.PENDING)

        val invitationForProfessional = Invitation(patientEmail, InvitationStatus.PENDING)

        return try {
            db.collection("users").document(professionalEmail)
                .update("invitations", FieldValue.arrayUnion(invitationForProfessional))
                .await()

            db.collection("users").document(patientEmail)
                .update("invitation", invitationForPatient)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("Firestore", "Error al enviar invitación", e)
            Result.failure(e)
        }
    }

    override suspend fun getInvitations(professionalEmail: String): Result<List<Invitation>> {
        return try {
            val userRef = db.collection("users").document(professionalEmail)
            val document = userRef.get().await() // Usa coroutines

            val invitations = document.get("invitations") as? List<Invitation> ?: emptyList()
            Result.success(invitations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfessionalInvitationList(
        professionalEmail: String,
        patientEmail: String,
        status: InvitationStatus
    ) {
        val professional = getUser(professionalEmail) ?: return

        val updatedInvitations = professional.invitations.map { invitation ->
            if (invitation.email == patientEmail) {
                invitation.copy(status = status)
            } else invitation
        }

        db.collection("users").document(professionalEmail)
            .update("invitations", updatedInvitations)
            .await()
    }
}
