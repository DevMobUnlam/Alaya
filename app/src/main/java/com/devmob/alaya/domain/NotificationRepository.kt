package com.devmob.alaya.domain

import retrofit2.Response

interface NotificationRepository {
    suspend fun sendNotificationInvitation(patientEmail: String, professionalEmail: String): Response<Unit>
    suspend fun sendNotificationNewTreatment(patientEmail: String, professionalEmail: String): Response<Unit>
}