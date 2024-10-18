package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User

interface UserFirestoreRepository {

    suspend fun addUser (user: User): FirebaseResult

}