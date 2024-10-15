package com.devmob.alaya.domain

import com.devmob.alaya.data.UserFirestoreRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User

class AddUserToFirestoreUseCase {

    private val userFirestoreRepository = UserFirestoreRepositoryImpl()

    suspend operator fun invoke(user: User): FirebaseResult {
        return userFirestoreRepository.addUser(user)
    }

}
