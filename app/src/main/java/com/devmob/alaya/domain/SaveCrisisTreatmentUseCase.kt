package com.devmob.alaya.domain

import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.data.NotificationRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import retrofit2.Response

class SaveCrisisTreatmentUseCase {
    private val customTreatmentRepository = CrisisTreatmentRepositoryImpl()
    private val notificationRepository = NotificationRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ): FirebaseResult {
        val result = customTreatmentRepository.addCustomTreatment(patientEmail, treatment)
        return result
    }

    suspend fun sendNotification(patientEmail: String, professionalEmail: String): Response<Unit> {
        return notificationRepository.sendNotificationNewTreatment(patientEmail,professionalEmail)
    }
}