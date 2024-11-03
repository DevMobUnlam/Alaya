package com.devmob.alaya.data

import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class CrisisRepositoryImpl : CrisisRepository {
    private val db = FirebaseClient().db
    private val auth = FirebaseClient().auth

    override suspend fun addRegister(register: CrisisDetailsDB): FirebaseResult = runCatching {
        auth.currentUser?.email?.let { db.collection("users").document(it).collection("crisis_registers").add(register).await() }
    }.toResponseFirebase()

    override suspend fun getLastCrisisDetails(): CrisisDetailsDB? {
        val querySnapshot = db.collection("users")
            .document(auth.currentUser?.email!!)
            .collection("crisis_registers")
            .orderBy("start", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].toObject(CrisisDetailsDB::class.java)
        } else {
            null
        }
    }
}
