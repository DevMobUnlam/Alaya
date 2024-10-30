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
            .update(fieldName, fieldValue).await()
    }

    override suspend fun addNewField(userId: String, fieldName: String, newField: Any) {
        db.collection("users").document(userId)
            .set(mapOf(fieldName to newField), SetOptions.merge()).await()
    }

    override suspend fun addNewFieldToList(userId: String, fieldName: String, newField: Any) {
        val document = db.collection("users").document(userId).get().await()
        val currentList = document.get(fieldName) as? List<Any> ?: emptyList()
        val updatedList = currentList + newField
        db.collection("users").document(userId)
            .update(fieldName, updatedList).await()
    }
}
