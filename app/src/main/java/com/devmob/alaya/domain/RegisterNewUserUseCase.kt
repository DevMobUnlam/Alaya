package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.AuthenticationResult

class RegisterNewUserUseCase(
    private val registerNewUserRepository: RegisterNewUserRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthenticationResult =
        registerNewUserRepository.createUserWithEmailAndPassword(email, password)
}