package com.devmob.alaya.domain

import com.devmob.alaya.data.SessionRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Session

class SessionUseCase {
    private val sessionRepository = SessionRepositoryImpl()

    suspend operator fun invoke(session: Session, patientEmail: String): FirebaseResult {
        return sessionRepository.addSession(session, patientEmail)
    }

    suspend fun getSessions(patientEmail: String): List<Session> {
        return sessionRepository.getSessions(patientEmail)
    }

    suspend fun getNextSession(patientEmail: String): Session? {
        return sessionRepository.getNextSession(patientEmail)
    }
}

