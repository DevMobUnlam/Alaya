package com.devmob.alaya.domain

import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.model.UserRole

class GetRoleUseCase {
    private val getUserRepository = GetUserRepositoryImpl()

    suspend operator fun invoke(email: String): UserRole? {
        return getUserRepository.getUser(email)?.role
    }
}
