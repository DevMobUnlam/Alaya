package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult

interface CrisisRepository {
    suspend fun addRegister(register: CrisisDetailsDB): FirebaseResult
}