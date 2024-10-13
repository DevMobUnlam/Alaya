package com.devmob.alaya.domain

import com.devmob.alaya.data.LoginRepositoryImpl
import com.devmob.alaya.domain.model.LoginResult

class LoginUseCase {
    private val loginRepository = LoginRepositoryImpl()

    suspend operator fun invoke(email: String, password: String): LoginResult =
        loginRepository.login(email, password)
}






