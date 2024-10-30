package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface CrisisRepository {
    suspend fun addRegister(register: CrisisDetailsDB): FirebaseResult
    suspend fun getRegisters(patientId: String): Flow<List<CrisisDetailsDB>?>
}