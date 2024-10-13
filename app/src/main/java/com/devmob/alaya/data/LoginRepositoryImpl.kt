package com.devmob.alaya.data

import com.devmob.alaya.domain.LoginRepository
import com.devmob.alaya.domain.model.LoginResult
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl : LoginRepository {
    private val firebase: FirebaseClient = FirebaseClient()
    override suspend fun login(email: String, pass: String): LoginResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(email, pass).await()
    }.toLoginResult()

    private fun Result<AuthResult>.toLoginResult(): LoginResult {
        return when {
            this.isSuccess -> {
                val userId = this.getOrNull()?.user
                checkNotNull(userId)
                LoginResult.Success
            }
            this.isFailure -> LoginResult.Error(checkNotNull(this.exceptionOrNull()))
            else -> LoginResult.Error(null)
        }
    }
}