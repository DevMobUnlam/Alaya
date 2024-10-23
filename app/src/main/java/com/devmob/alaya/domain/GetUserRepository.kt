package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.User
import com.google.firebase.firestore.Source

interface GetUserRepository {
    suspend fun getUser(email: String, source: Source): User?
}