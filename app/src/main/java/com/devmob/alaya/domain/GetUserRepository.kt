package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.User

interface GetUserRepository {
    suspend fun getUser(email: String): User?
}