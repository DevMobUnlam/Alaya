package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.AuthenticationResult

interface  RegisterNewUserRepository {

    suspend fun createUserWithEmailAndPassword(user: String, password: String): AuthenticationResult

}
