package com.devmob.alaya.domain

import android.icu.util.Calendar
import android.util.Log
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
    private val crisisRepository: CrisisRepository,
    private val getUserRepository: GetUserRepository,
    ) {

    suspend operator fun invoke(
        instructions: String,
        generativeModel: GenerativeModel,
        patientId: String
): Flow<String> {

        return flow {

            val patientName =  getUserRepository.getUser(patientId)?.name


            crisisRepository.getRegisters(patientId).collect { crisisDetailsDBList ->
                val json = gson.toJson(crisisDetailsDBList?.map { it.toIASummaryModel("$patientName") })
                val prompt = "$instructions \n $json"

                Log.i("GetIASummaryUseCase", json)
                if(json.equals("[]")){
                    emit("")
                }else{
                    emit(generativeModel.generateContent(prompt).text?: "")
                }
            }
        }
    }
}
