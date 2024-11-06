package com.devmob.alaya.domain

import android.content.Context
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.devmob.alaya.R
import com.devmob.alaya.data.mapper.toIASummaryModel
import com.devmob.alaya.utils.toCalendar
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class GetIASummaryUseCase @Inject constructor(
    private val gson: Gson,
    private val generativeModel: GenerativeModel,
    private val crisisRepository: CrisisRepository,
    private val getUserRepository: GetUserRepository,
    ) {

    suspend operator fun invoke(
        instructions: String,

        patientId: String,
        onRegisterUpdate: () -> Unit,
): Flow<String> {

        return flow {


            val patientName =  getUserRepository.getUser(patientId)?.name

            crisisRepository.getRegisters(patientId, onRegisterUpdate).collect { crisisDetailsDBList ->

                val mappedList = crisisDetailsDBList?.map { it.toIASummaryModel() }


                if(mappedList?.isEmpty() == true){
                    emit("")
                }else{
                    val json = gson.toJson(IASummaryPrompt(patientName = patientName?:"", input = mappedList?: emptyList()))
                    Log.v("getiasummary", json)

                    val prompt = "$instructions \n $json"
                    emit(generativeModel.generateContent(prompt).text?: "")
                }
            }
        }
    }
}

