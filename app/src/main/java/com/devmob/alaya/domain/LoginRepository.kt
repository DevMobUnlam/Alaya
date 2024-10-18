package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.AuthenticationResult

interface LoginRepository {
    suspend fun login(email: String, pass: String): AuthenticationResult
}