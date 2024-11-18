package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toAuthenticationResult
import com.devmob.alaya.domain.RegisterNewUserRepository
import com.devmob.alaya.domain.model.AuthenticationResult
import kotlinx.coroutines.tasks.await

class RegisterNewUserRepositoryImpl(
    firebaseClient: FirebaseClient
) : RegisterNewUserRepository {
    private val auth = firebaseClient.auth
    override suspend fun createUserWithEmailAndPassword(user: String, password: String): AuthenticationResult = runCatching {
        auth.createUserWithEmailAndPassword(user, password).await()
    }.toAuthenticationResult()
}