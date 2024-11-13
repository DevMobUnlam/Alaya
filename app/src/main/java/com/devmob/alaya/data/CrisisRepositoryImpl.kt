package com.devmob.alaya.data

import android.util.Log
import androidx.compose.animation.core.snap
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import com.google.firebase.firestore.Query
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date

class CrisisRepositoryImpl : CrisisRepository {
    private val db = FirebaseClient().db
    private val auth = FirebaseClient().auth

    override suspend fun addRegister(register: CrisisDetailsDB): FirebaseResult = runCatching {
        auth.currentUser?.email?.let {
            db.collection("users").document(it).collection("crisis_registers").add(register).await()
        }
    }.toResponseFirebase()

    override suspend fun getRegisters(patientId: String): Flow<List<CrisisDetailsDB>?> {
        return callbackFlow{

            try {

                val registersCollection =
                    db.collection("users").document(patientId).collection("crisis_registers")

                registersCollection.orderBy("start", Query.Direction.DESCENDING)
                    .whereEqualTo("completed", true)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { documents ->
                        if(!documents.isEmpty){
                            val latestDate = documents.documents[0].getDate("start")
                            val calendar = Calendar.getInstance()
                            calendar.time = latestDate?:Date()
                            calendar.add(Calendar.DAY_OF_YEAR, -7)
                            val startDate = calendar.time

                            registersCollection
                                .whereEqualTo("completed", true)
                                .whereGreaterThanOrEqualTo("start", startDate)
                                .orderBy("start", Query.Direction.ASCENDING)
                                .addSnapshotListener{snapshot, e ->
                                    if(e != null){
                                        Log.w("Firebase", "Crisis listen failed", e)
                                        this.close()
                                    }
                                    if(snapshot != null && !snapshot.isEmpty){


                                        val register = snapshot.map { it.toObject(CrisisDetailsDB::class.java) }
                                        this.trySend(register)
                                    }else{
                                        Log.w("Firebase", "Data not found")
                                        this.trySend(emptyList())
                                    }

                                }
                        }else{
                            Log.i("CrisisRepositoryImpl", "Collection is empty")
                            this.trySend(emptyList())
                        }
                    }

            } catch (e: Exception) {
                Log.e("Firebase", e.localizedMessage ?: "")
            }
            awaitClose { this.cancel() }
        }


    }

    override suspend fun getLastCrisisDetails(): CrisisDetailsDB? {
        val userEmail = auth.currentUser?.email
        if (userEmail == null) {
            Log.d("CrisisRepository", "El usuario no está autenticado.")
            return null
        } else {

        val querySnapshot = db.collection("users")
            .document(userEmail)
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
    }


    override suspend fun updateCrisisDetails(register: CrisisDetailsDB): FirebaseResult {
        val userEmail = auth.currentUser?.email
        if (userEmail == null) {
            Log.d("CrisisRepository", "El usuario no está autenticado.")
            return FirebaseResult.Error(IllegalStateException("El usuario no está autenticado"))
        }

        val querySnapshot = db.collection("users")
            .document(userEmail)
            .collection("crisis_registers")
            .whereEqualTo("completed", false)
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

