package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl

class GetUserDataUseCase {
    private val getUserRepository = GetUserRepositoryImpl()

    suspend fun getName(email: String): String? {
        return getUserRepository.getUser(email)?.name
    }

    suspend fun getSurname(email: String): String? {
        return getUserRepository.getUser(email)?.surname
    }
}
