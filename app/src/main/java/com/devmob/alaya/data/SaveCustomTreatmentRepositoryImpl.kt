package com.devmob.alaya.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.SaveCustomTreatmentRepository
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import kotlinx.coroutines.tasks.await

class SaveCustomTreatmentRepositoryImpl : SaveCustomTreatmentRepository {
    val db = FirebaseClient().db

    override suspend fun addCustomTreatment(
        patientEmail: String,
        treatment: SnapshotStateList<OptionTreatment>
    ): FirebaseResult = runCatching {
        db.collection("users").document(patientEmail).collection("custom_treatment").add(treatment)
            .await()
    }.toResponseFirebase()
}