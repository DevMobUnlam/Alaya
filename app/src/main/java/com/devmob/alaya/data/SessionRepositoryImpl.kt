package com.devmob.alaya.data

import android.util.Log
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.GetUserDataUseCase
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
    private val getUserRepository = GetUserRepositoryImpl(firebase)
    private val getUserDataUseCase = GetUserDataUseCase(getUserRepository)

    override suspend fun addSession(session: Session, patientEmail: String): FirebaseResult = runCatching {
        firebase.auth.currentUser?.email?.let {
            try {
                val professional = getUserDataUseCase.getUser(it)
                val patients = professional?.patients?.toMutableList()
                    ?: throw IllegalArgumentException("Lista de pacientes no encontrada.")

                val patient = patients.firstOrNull { p -> p.email == patientEmail }
                    ?: throw IllegalArgumentException("Paciente no encontrado con el email: $patientEmail.")

                val newPatient = patient.copy(nextSession = session.date)
                val index = patients.indexOf(patient)
                patients[index] = newPatient

                val sessionId = UUID.randomUUID().toString()
                val sessionRef = db.collection("users")
                    .document(patientEmail)
                    .collection("sessions")
                    .document(sessionId)
                val sessionProfessionalRef = db.collection("users")
                    .document(it)

                db.runBatch { batch ->
                    batch.set(sessionRef, session)
                    batch.update(sessionProfessionalRef, "patients", patients)
                }
            } catch (e: Exception) {
                Log.e("addSession", "Error al agregar sesión: ${e.message}", e)
                throw e
            }
        } ?: throw IllegalStateException("Usuario no autenticado.")
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