package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.UserRole

class GetRoleUseCase(
    private val getUserRepository: GetUserRepository
) {
    suspend operator fun invoke(email: String): UserRole? {
        return getUserRepository.getUser(email)?.role
    }
}
