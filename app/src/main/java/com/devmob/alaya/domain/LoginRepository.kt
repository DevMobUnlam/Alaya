package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.LoginResult

interface LoginRepository {
    suspend fun login(email: String, pass: String): LoginResult
}