package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.CrisisTreatmentRepository
import com.devmob.alaya.domain.UploadImageToFirestoreUseCase
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.domain.model.User
import kotlinx.coroutines.tasks.await

class CrisisTreatmentRepositoryImpl(
    firebaseClient: FirebaseClient,
    private val uploadImage: UploadImageToFirestoreUseCase
) : CrisisTreatmentRepository {
    private val db = firebaseClient.db

    override suspend fun saveCustomTreatment(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ) = runCatching {
        val treatmentList = mutableListOf<OptionTreatment>()

        treatment.forEach { optionTreatment ->
            optionTreatment?.imageUri?.let { imageUri ->
                val image = uploadImage(imageUri)
                if (image != null) {
                    val updatedTreatment = optionTreatment.copy(imageUri = image.toString())
                    treatmentList.add(updatedTreatment)
                } else {
                    val updatedTreatment = optionTreatment.copy(imageUri = "")
                    treatmentList.add(updatedTreatment)
                }
            }
        }
        db.collection("users").document(patientEmail)
            .update("stepCrisis", treatment)
    }.toResponseFirebase()

    override suspend fun getCustomTreatment(patientEmail: String): List<OptionTreatment>? {
        val snapshot = db.collection("users").document(patientEmail).get().await()
        val treatmentList = snapshot.toObject(User::class.java)?.stepCrisis
        return treatmentList
    }
}
