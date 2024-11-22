package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.SessionRepository
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Session
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class SessionRepositoryImpl : SessionRepository {
    private val firebase: FirebaseClient = FirebaseClient()
    private val db = FirebaseClient().db

    override suspend fun addSession(session: Session, patientEmail: String): FirebaseResult = runCatching {
        firebase.auth.currentUser?.email?.let {
            val sessionId = UUID.randomUUID().toString()
            val sessionRef = db.collection("users")
                .document(patientEmail)
                .collection("sessions")
                .document(sessionId)
            sessionRef.set(session).await()
        }
    }.toResponseFirebase()

    override suspend fun getSessions(patientEmail: String): List<Session> = runCatching {
        val querySnapshot = db.collection("users")
            .document(patientEmail)
            .collection("sessions")
            .get()
            .await()

        querySnapshot.documents.map { doc ->
            doc.toObject(Session::class.java) ?: Session()
        }
    }.getOrElse { emptyList() }

    override suspend fun getNextSession(patientEmail: String): Session? = runCatching {
        val querySnapshot = db.collection("users")
            .document(patientEmail)
            .collection("sessions")
            .whereGreaterThan("date", Date())
            .orderBy("date", Query.Direction.ASCENDING)
            .limit(1)
            .get()
            .await()

        querySnapshot.documents.firstOrNull()?.toObject(Session::class.java)
    }.getOrElse { null }
}