package com.devmob.alaya.data

import android.util.Log
import com.devmob.alaya.data.mapper.toUser
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUserRepositoryImpl @Inject constructor(
    firebaseClient: FirebaseClient
) : GetUserRepository {
    private val db = firebaseClient.db

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

    override suspend fun sendInvitation(
        invitationForPatient: Invitation,
        invitationForProfessional: Invitation
    ): Result<Unit> {
        return try {
            val professionalRef = db.collection("users").document(invitationForPatient.email)
            val patientRef = db.collection("users").document(invitationForProfessional.email)
            db.runBatch { batch ->
                batch.update(professionalRef,"invitations",FieldValue.arrayUnion(invitationForProfessional))
                batch.update(patientRef,"invitation", invitationForPatient)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("Firestore", "Error al enviar invitación", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProfileImage(userId: String, imageUrl: String): Boolean {
        return try {
            db.collection("users").document(userId)
                .update("profileImage", imageUrl).await()
            true
        } catch (e: Exception) {
            Log.e("Firestore", "Error actualizando imagen de perfil", e)
            false
        }
    }

    override suspend fun updatePhoneNumber(userId: String, phoneNumber: String): Boolean {
        return try {
            db.collection("users").document(userId)
                .update("phone", phoneNumber).await()
            true
        } catch (e: Exception) {
            Log.e("Firestore", "Error actualizando número de teléfono", e)
            false
        }
    }
}

