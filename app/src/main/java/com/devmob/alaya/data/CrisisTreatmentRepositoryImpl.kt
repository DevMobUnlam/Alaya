package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.CrisisTreatmentRepository
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.domain.model.User
import kotlinx.coroutines.tasks.await

class CrisisTreatmentRepositoryImpl : CrisisTreatmentRepository {
    private val db = FirebaseClient().db

    override suspend fun saveCustomTreatment(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ) = runCatching {
        db.collection("users").document(patientEmail)
            .update("stepCrisis", treatment)
    }.toResponseFirebase()

    override suspend fun getCustomTreatment(patientEmail: String): List<OptionTreatment>? {
        val snapshot = db.collection("users").document(patientEmail).get().await()
        val treatmentList = snapshot.toObject(User::class.java)?.stepCrisis
        return treatmentList
    }
}
