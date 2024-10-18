package com.devmob.alaya.domain

import com.devmob.alaya.data.RegisterNewUserRepositoryImpl
import com.devmob.alaya.domain.model.AuthenticationResult

class RegisterNewUserUseCase {
    private val registerNewUserRepository = RegisterNewUserRepositoryImpl()

    suspend operator fun invoke(email: String, password: String) : AuthenticationResult =
        registerNewUserRepository.createUserWithEmailAndPassword(email, password)

}