package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.model.User

class GetUserDataUseCase {
    private val getUserRepository = GetUserRepositoryImpl()

    suspend fun getUser(email: String): User? {
        return getUserRepository.getUser(email)
    }

    suspend fun getName(email: String): String? {
        return getUserRepository.getUser(email)?.name
    }

    suspend fun getSurname(email: String): String? {
        return getUserRepository.getUser(email)?.surname
    }

    suspend fun getPhone(email: String): String? {
        return getUserRepository.getUser(email)?.phone
    }
}
