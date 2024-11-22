package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface CrisisRepository {
    suspend fun addRegister(register: CrisisDetailsDB): FirebaseResult
    suspend fun getRegisters(patientId: String, onRegisterUpdate:() -> Unit): Flow<List<CrisisDetailsDB>?>
    suspend fun getLastCrisisDetails(): CrisisDetailsDB?
    suspend fun updateCrisisDetails(register: CrisisDetailsDB): FirebaseResult
    suspend fun getListRegisters(patientId: String): List<CrisisDetailsDB>?
}