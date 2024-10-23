package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toUser
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.User
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class GetUserRepositoryImpl : GetUserRepository {
    private val db = FirebaseClient().db

    override suspend fun getUser(email: String, source: Source): User? = runCatching {
        db.collection("users").document(email).get(source).await()
    }.toUser()
}
