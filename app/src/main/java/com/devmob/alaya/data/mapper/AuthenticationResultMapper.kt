package com.devmob.alaya.data.mapper

import com.devmob.alaya.domain.model.AuthenticationResult
import com.google.firebase.auth.AuthResult

fun Result<AuthResult>.toAuthenticationResult(): AuthenticationResult {
    return when {
        this.isSuccess -> {
            val userId = this.getOrNull()?.user
            checkNotNull(userId)
            AuthenticationResult.Success
        }

        this.isFailure -> AuthenticationResult.Error(checkNotNull(this.exceptionOrNull()))
        else -> AuthenticationResult.Error(null)
    }
}