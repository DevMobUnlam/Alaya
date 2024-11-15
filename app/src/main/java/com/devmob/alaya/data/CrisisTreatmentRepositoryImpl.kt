package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.CrisisTreatmentRepository
import com.devmob.alaya.domain.model.OptionTreatment

class CrisisTreatmentRepositoryImpl(
    firebaseClient: FirebaseClient
) : CrisisTreatmentRepository {
    private val db = firebaseClient.db
    override suspend fun saveCustomTreatment(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ) = runCatching {
        db.collection("users").document(patientEmail)
            .update("stepCrisis", treatment)
    }.toResponseFirebase()
}
