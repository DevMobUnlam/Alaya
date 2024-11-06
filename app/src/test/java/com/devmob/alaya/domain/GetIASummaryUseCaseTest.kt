package com.devmob.alaya.domain

import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetIASummaryUseCaseTest{


    //TODO si no existe el usuario
//    @Test
//    fun `when patient doesn't exist then return an emptyList`(): Unit = runBlocking {
//        //GIVEN
//        val patientID = ""
//            coEvery{crisisRepository.getRegisters(patientId = patientID, onRegisterUpdate = {})} returns flow{emit(
//                emptyList()
//            )}
//
//        //WHEN
//
//        getIASummaryUseCase(instructions = "",onRegisterUpdate = {}, patientId = patientID)
//
//        //THEN
//
//    }


    //TODO si no existen registros de crisis
    //TODO si existen registros de crisis
}