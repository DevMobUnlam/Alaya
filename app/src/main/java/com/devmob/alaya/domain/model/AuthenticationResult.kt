package com.devmob.alaya.domain.model

import androidx.compose.ui.semantics.Role

sealed class AuthenticationResult {
    data class Error(val t: Throwable?) : AuthenticationResult()
    data object Success /*(val role: UserRole)*/ : AuthenticationResult()
}
