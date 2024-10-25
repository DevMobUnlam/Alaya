package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl

class GetUserSurnameUseCase {
    private val getUserRepository = GetUserRepositoryImpl()

    suspend operator fun invoke(email: String): String? {
        return getUserRepository.getUser(email)?.surname
    }
}