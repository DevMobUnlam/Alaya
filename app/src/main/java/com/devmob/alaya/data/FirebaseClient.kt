package com.devmob.alaya.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Singleton

@Singleton
class FirebaseClient {
    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
}