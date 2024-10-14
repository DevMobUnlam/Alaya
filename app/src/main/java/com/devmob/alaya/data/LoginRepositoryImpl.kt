package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toAuthenticationResult
import com.devmob.alaya.domain.LoginRepository
import com.devmob.alaya.domain.model.AuthenticationResult
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl : LoginRepository {
    private val firebase: FirebaseClient = FirebaseClient()
    override suspend fun login(email: String, pass: String): AuthenticationResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(email, pass).await()
    }.toAuthenticationResult()


}