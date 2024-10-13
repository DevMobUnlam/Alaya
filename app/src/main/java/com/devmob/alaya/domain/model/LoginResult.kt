package com.devmob.alaya.domain.model

sealed class LoginResult {
    data class Error(val t: Throwable?) : LoginResult()
    data object Success/*(val role: Role)*/ : LoginResult()
}
