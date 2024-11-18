package com.devmob.alaya.data

import com.devmob.alaya.ONESIGNAL_APP_ID
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.NotificationRepository
import retrofit2.Response

class NotificationRepositoryImpl (
    private val api: NotificationService,
    private val firebaseClient: FirebaseClient,
    private val getUserData: GetUserDataUseCase
): NotificationRepository {



    override suspend fun sendNotificationInvitation(
        patientEmail: String
    ): Response<Unit> {

        val currentUser = firebaseClient.auth.currentUser?.email
        val professional = currentUser?.let { getUserData.getUser(it) }
        val completeName = professional?.name + " " + professional?.surname

        val body = NotificationInvitation(
            ONESIGNAL_APP_ID,
            "push",
            contents = mapOf("en" to "$completeName te invitó a que seas su paciente a traves de Alaya."),
            include_aliases = IncludeAliases(
                ALIAS_FIREBASE_ID = listOf(patientEmail)
            )
        )
        return api.sendNotification(body)
    }

    override suspend fun sendNotificationNewTreatment(
        patientEmail: String
    ): Response<Unit> {
        val currentUser = firebaseClient.auth.currentUser?.email
        val professional = currentUser?.let { getUserData.getUser(it) }

        val body = NotificationInvitation(
            ONESIGNAL_APP_ID,
            "push",
            contents = mapOf("en" to "${professional?.name} te configuró un nuevo tratamiento."),
            include_aliases = IncludeAliases(
                ALIAS_FIREBASE_ID = listOf(patientEmail)
            )
        )
        return api.sendNotification(body)
    }
}