package com.devmob.alaya.data

import com.devmob.alaya.ONESIGNAL_APP_ID
import com.devmob.alaya.domain.NotificationRepository
import retrofit2.Response

class NotificationRepositoryImpl : NotificationRepository {

    private val api = NotificationService()
    override suspend fun sendNotificationInvitation(
        patientEmail: String,
        professionalEmail: String
    ): Response<Unit> {
        val body = NotificationInvitation(
            ONESIGNAL_APP_ID,
            "push",
            contents = mapOf("en" to "$professionalEmail te invitó a que seas su paciente a traves de Alaya."),
            include_aliases = IncludeAliases(
                ALIAS_FIREBASE_ID = listOf(patientEmail)
            )
        )
        return api.sendNotification(body)
    }
}