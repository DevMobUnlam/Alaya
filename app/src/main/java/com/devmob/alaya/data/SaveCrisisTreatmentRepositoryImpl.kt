package com.devmob.alaya.data

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.SaveCrisisTreatmentRepository
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import kotlinx.coroutines.tasks.await

class SaveCrisisTreatmentRepositoryImpl : SaveCrisisTreatmentRepository {
    private val db = FirebaseClient().db

    override suspend fun addCustomTreatment(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ): FirebaseResult = runCatching {
        Log.d("leandro", "repoimpl usuario $patientEmail")
        db.collection("users").document(patientEmail).update("crisis_treatment", treatment).await()
    }.toResponseFirebase()
}