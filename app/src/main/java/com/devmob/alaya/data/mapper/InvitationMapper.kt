package com.devmob.alaya.data.mapper

import com.devmob.alaya.domain.model.Invitation
import com.google.firebase.firestore.DocumentSnapshot

fun Result<DocumentSnapshot>.toInvitation(): Invitation? {
    this.getOrNull()?.let {
        return it.toObject(Invitation::class.java)
    }
    return null
}