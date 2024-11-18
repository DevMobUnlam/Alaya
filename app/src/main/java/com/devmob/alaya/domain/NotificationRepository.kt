package com.devmob.alaya.domain

import retrofit2.Response

interface NotificationRepository {
    suspend fun sendNotificationInvitation(patientEmail: String): Response<Unit>
    suspend fun sendNotificationNewTreatment(patientEmail: String): Response<Unit>
}