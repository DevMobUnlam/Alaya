package com.devmob.alaya.data.mapper

import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UserRole
import com.google.firebase.firestore.DocumentSnapshot

fun Result<DocumentSnapshot>.toUser(): User? {
    var user: User? = null
    this.onSuccess { db ->
        user = User(
            db.get("name") as String,
            db.get("surname") as String,
            db.get("email") as String,
            //it.get("image") as Int,
            hour = db.get("hour") as String,
            role = (db.get("role") as String).let {
                if (it == "PROFESSIONAL") {
                    UserRole.PROFESSIONAL
                } else UserRole.PATIENT
            }
        )
    }.onFailure {
        user = null
    }
    return user
}
