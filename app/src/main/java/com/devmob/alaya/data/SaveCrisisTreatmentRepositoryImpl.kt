package com.devmob.alaya.data

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.SaveCrisisTreatmentRepository
import com.devmob.alaya.domain.UploadImageToFirestoreUseCase
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import kotlinx.coroutines.tasks.await

class SaveCrisisTreatmentRepositoryImpl : SaveCrisisTreatmentRepository {
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

                if (image != null) {
                    val updatedTreatment = optionTreatment.copy(imageUri = image)
                    treatmentList.add(updatedTreatment)
                } else {
                    val updatedTreatment = optionTreatment.copy(imageUri = Uri.EMPTY)
                    treatmentList.add(updatedTreatment)
                }
            }
        }
        db.collection("users").document(patientEmail)
            .update("crisis_treatment", treatmentList)
    }.toResponseFirebase()
}