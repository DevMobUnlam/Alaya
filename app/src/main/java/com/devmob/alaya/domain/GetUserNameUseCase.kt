package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl

class GetUserNameUseCase {
    private val getUserRepository = GetUserRepositoryImpl()

    suspend operator fun invoke(email: String): String? {
        return getUserRepository.getUser(email)?.name
    }
}
