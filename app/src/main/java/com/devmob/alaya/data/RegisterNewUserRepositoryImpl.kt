package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toAuthenticationResult
import com.devmob.alaya.domain.RegisterNewUserRepository
import com.devmob.alaya.domain.model.AuthenticationResult
import com.google.android.gms.tasks.Tasks.await
import kotlinx.coroutines.tasks.await

class RegisterNewUserRepositoryImpl : RegisterNewUserRepository {
    private val auth = FirebaseClient().auth

    override suspend fun createUserWithEmailAndPassword(user: String, password: String): AuthenticationResult = runCatching {
        auth.createUserWithEmailAndPassword(user, password).await()
    }.toAuthenticationResult()
}