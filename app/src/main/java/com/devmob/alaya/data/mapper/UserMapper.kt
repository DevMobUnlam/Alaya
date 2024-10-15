package com.devmob.alaya.data.mapper

import com.devmob.alaya.domain.model.User
import com.google.firebase.firestore.DocumentSnapshot

fun Result<DocumentSnapshot>.toUser(): User? {
    this.getOrNull()?.let {
        return it.toObject(User::class.java)
    }
    return null
}
