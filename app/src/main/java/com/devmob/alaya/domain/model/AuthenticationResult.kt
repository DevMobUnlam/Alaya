package com.devmob.alaya.domain.model

sealed class AuthenticationResult {
    data class Error(val t: Throwable?) : AuthenticationResult()
    data object Success : AuthenticationResult()
}
