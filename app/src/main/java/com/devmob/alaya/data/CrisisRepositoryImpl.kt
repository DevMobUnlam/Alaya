package com.devmob.alaya.data

import android.util.Log
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
        auth.currentUser?.email?.let {
            db.collection("users").document(it).collection("crisis_registers").add(register).await()
        }
    }.toResponseFirebase()

    override suspend fun getLastCrisisDetails(): CrisisDetailsDB? {
        val querySnapshot = db.collection("users")
            .document(auth.currentUser?.email!!)
            .collection("crisis_registers")
            .whereEqualTo("completed", false)
            .orderBy("start", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        Log.d("CrisisRepository", "Consulta Firestore realizada, resultados encontrados: ${querySnapshot.size()}")

        return if (querySnapshot.isEmpty) {
            Log.d("CrisisRepository", "No se encontraron registros incompletos.")
            null
        } else {
            val crisisDetails = querySnapshot.documents[0].toObject(CrisisDetailsDB::class.java)
            Log.d("CrisisRepository", "Última crisis encontrada: ${crisisDetails?.start} - ${crisisDetails?.completed}")
            crisisDetails
        }
    }

    override suspend fun updateCrisisDetails(register: CrisisDetailsDB): FirebaseResult {
        val querySnapshot = db.collection("users")
            .document(auth.currentUser?.email!!)
            .collection("crisis_registers")
            .whereEqualTo("completed", false) // Filtro por registros incompletos
            .orderBy("start", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            Log.d("registro", "No se encontró un registro incompleto")
            return FirebaseResult.Error(IllegalStateException("No hay registros incompletos"))
        }

        val crisisRef = querySnapshot.documents[0].reference

        return try {
            crisisRef.set(register.copy(completed = true)).await()
            FirebaseResult.Success
        } catch (e: Exception) {
            FirebaseResult.Error(e)
        }
    }
}


