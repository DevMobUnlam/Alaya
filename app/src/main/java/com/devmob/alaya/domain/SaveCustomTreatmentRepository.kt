package com.devmob.alaya.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

interface SaveCustomTreatmentRepository {
    suspend fun addCustomTreatment(patientEmail: String, treatment: SnapshotStateList<OptionTreatment>) : FirebaseResult
}