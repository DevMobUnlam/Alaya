package com.devmob.alaya.data

import android.util.Log
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult
import kotlinx.coroutines.tasks.await

class CrisisRepositoryImpl : CrisisRepository {
    private val db = FirebaseClient().db
    private val auth = FirebaseClient().auth
    override suspend fun addRegister(register: CrisisDetailsDB): FirebaseResult = runCatching {
        auth.currentUser?.email?.let { db.collection("users").document(it).collection("crisis_registers").add(register).await() }
    }.toResponseFirebase()

    override suspend fun getRegisters(patientId: String): List<CrisisDetailsDB>? {
        try{
            val registersRef = db.collection("users").document(patientId).collection("crisis_registers")
            val registers = registersRef.get().await()?.map{
                it.toObject(CrisisDetailsDB::class.java)
            }

            return registers
        }catch(e:Exception){
            Log.e("Firebase",e.localizedMessage?:"")
            return emptyList()
        }


    }

}
