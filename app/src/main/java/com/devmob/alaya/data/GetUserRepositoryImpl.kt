package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toUser
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.User
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class GetUserRepositoryImpl : GetUserRepository {
    private val db = FirebaseClient().db

    override suspend fun getUser(email: String): User? = runCatching {
        db.collection("users").document(email).get().await()
    }.toUser()

    override suspend fun updateUserField(userId: String, fieldName: String, fieldValue: Any) {
        db.collection("users").document(userId)
            .set(mapOf(fieldName to fieldValue), SetOptions.merge()).await()
    }
}
