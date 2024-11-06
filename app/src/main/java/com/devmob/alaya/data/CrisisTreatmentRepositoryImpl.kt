package com.devmob.alaya.data

import android.net.Uri
import android.util.Log
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.CrisisTreatmentRepository
import com.devmob.alaya.domain.UploadImageToFirestoreUseCase
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.domain.model.User
import kotlinx.coroutines.tasks.await

class CrisisTreatmentRepositoryImpl : CrisisTreatmentRepository {
    private val db = FirebaseClient().db
    private val uploadImage = UploadImageToFirestoreUseCase()

    override suspend fun addCustomTreatment(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ) = runCatching {
        Log.d("leandro", "repoimpl usuario $patientEmail")
        val treatmentList = mutableListOf<OptionTreatment>()


        treatment.forEach { optionTreatment ->
            optionTreatment?.imageUri?.let { imageUri ->
                val image = uploadImage(imageUri)

                Log.d("leandro123", "EL path de la foto es $image")
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
            .update("crisis_treatment", treatmentList)
    }.toResponseFirebase()

    override suspend fun getCustomTreatment(patientEmail: String): List<OptionTreatment>? {
        val snapshot = db.collection("users").document(patientEmail).get().await()
        val treatmentList = snapshot.toObject(User::class.java)?.crisis_treatment

        Log.d("leandro", "la lista desde firebase: $treatmentList")

        return treatmentList
    }
}
