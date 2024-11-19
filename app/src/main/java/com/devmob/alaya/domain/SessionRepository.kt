package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Session

interface SessionRepository {
    suspend fun addSession(session: Session, patientEmail: String): FirebaseResult
    suspend fun getSessions(patientEmail: String): List<Session>
}