package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toAuthenticationResult
import com.devmob.alaya.domain.LoginRepository
import com.devmob.alaya.domain.model.AuthenticationResult
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl(
    private val firebase: FirebaseClient
) : LoginRepository {
    override suspend fun login(email: String, pass: String): AuthenticationResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(email, pass).await()
    }.toAuthenticationResult()

}