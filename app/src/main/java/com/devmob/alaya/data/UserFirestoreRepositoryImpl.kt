package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.UserFirestoreRepository
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import kotlinx.coroutines.tasks.await

class UserFirestoreRepositoryImpl : UserFirestoreRepository{

    private val db = FirebaseClient().db

    override suspend fun addUser(user: User) : FirebaseResult = runCatching {
        db.collection("users").document(user.email).set(user).await()
    }.toResponseFirebase()

}

