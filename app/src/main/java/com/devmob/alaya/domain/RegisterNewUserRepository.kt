package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.AuthenticationResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface  RegisterNewUserRepository {

    suspend fun createUserWithEmailAndPassword(user: String, password: String): AuthenticationResult

}
