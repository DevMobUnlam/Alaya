package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Session
import retrofit2.Response

class SessionUseCase(
    private val notificationRepository : NotificationRepository,
    private val sessionRepository : SessionRepository
) {
    suspend operator fun invoke(session: Session, patientEmail: String): FirebaseResult {
        return sessionRepository.addSession(session, patientEmail)
    }

    suspend fun getSessions(patientEmail: String): List<Session> {
        return sessionRepository.getSessions(patientEmail)
    }

    suspend fun getNextSession(patientEmail: String): Session? {
        return sessionRepository.getNextSession(patientEmail)
    }

    suspend fun sendNotificationSession(patientEmail: String): Response<Unit> {
        return notificationRepository.sendNotificationSessions(patientEmail)
    }
}

