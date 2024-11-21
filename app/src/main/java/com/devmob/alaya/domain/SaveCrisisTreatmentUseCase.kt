package com.devmob.alaya.domain

import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.NotificationRepositoryImpl
import com.devmob.alaya.data.NotificationService
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import retrofit2.Response

class SaveCrisisTreatmentUseCase(
    private val customTreatmentRepository: CrisisTreatmentRepository,
    private val uploadImage: UploadImageToFirestoreUseCase,
    private val notificationRepository : NotificationRepository
) {
    suspend operator fun invoke(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ): FirebaseResult {
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
        return customTreatmentRepository.saveCustomTreatment(patientEmail, treatmentList)
    }

    suspend fun sendNotification(patientEmail: String): Response<Unit> {
        return notificationRepository.sendNotificationNewTreatment(patientEmail)
    }
}